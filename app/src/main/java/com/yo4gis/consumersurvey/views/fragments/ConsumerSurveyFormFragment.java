package com.yo4gis.consumersurvey.views.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.yo4gis.consumersurvey.R;
import com.yo4gis.consumersurvey.databinding.FragmentConsumerSurveyFormBinding;
import com.yo4gis.consumersurvey.listeners.CommonResponseNavigator;
import com.yo4gis.consumersurvey.model.LocationInfo;
import com.yo4gis.consumersurvey.model.PostBuilding;
import com.yo4gis.consumersurvey.model.PostConsumerCreate;
import com.yo4gis.consumersurvey.network.ApiFactory;
import com.yo4gis.consumersurvey.network.NetworkConnectionInterceptor;
import com.yo4gis.consumersurvey.repositories.BuildingRepository;
import com.yo4gis.consumersurvey.repositories.ConsumerRepository;
import com.yo4gis.consumersurvey.utilities.AppUtils;
import com.yo4gis.consumersurvey.utilities.Global;
import com.yo4gis.consumersurvey.viewModel.BuildingViewModel;
import com.yo4gis.consumersurvey.viewModel.ConsumerViewModel;
import com.yo4gis.consumersurvey.views.factories.BuildingFactory;
import com.yo4gis.consumersurvey.views.factories.ConsumerFactory;
import com.yo4gis.consumersurvey.views.listeners.BasicFragmentListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ConsumerSurveyFormFragment extends Fragment implements CommonResponseNavigator {
    private FragmentConsumerSurveyFormBinding binding;
    BasicFragmentListener frListener;
    ConsumerFactory factory;
    ConsumerRepository repository;
    ConsumerViewModel model;
    private int GALLERY = 1, CAMERA = 2, VIEW = 3, CHOOSER = 4,CAMERABILL=5;
    private int GALLERY_CROP = 111;
    private String mCurrentPhotoPath;
    public static Uri photoURI;
    static String extension;
    public static Bitmap thumbnail;
    public static Bitmap thumbnail1;
    public static Uri contentURI;
    public ConsumerSurveyFormFragment() {
        // Required empty public constructor
    }

    public static ConsumerSurveyFormFragment newInstance(String param1, String param2) {
        ConsumerSurveyFormFragment fragment = new ConsumerSurveyFormFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_consumer_survey_form, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView();
        extension = "";
        binding.imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showPictureDialog();
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (Global.isConnected(getActivity()))
                        requestPermission();
                    //else
                    //  AlertDialogUtil.errorAlertDialog("", getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                } else {
                    takePhotoFromCamera();
                }
            }
        });
        binding.imgCamerabill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showPictureDialog();
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (Global.isConnected(getActivity()))
                        requestPermission();
                    //else
                    //  AlertDialogUtil.errorAlertDialog("", getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                } else {
                    takePhotoForBillingFromCamera();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        frListener = (BasicFragmentListener) getActivity();
        frListener.setTitle(getResources().getString(R.string.consumer_survey));
        frListener.isTopBar(true);
    }

    private void initializeView() {
        try {
            repository = new ConsumerRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory = new ConsumerFactory(getActivity(), repository);
        model = ViewModelProviders.of(this, factory).get(ConsumerViewModel.class);
        model.navigator = this;
        model.apiBuildingID();
        initializeDropdown();
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.apiConsumerCreate(getPostConsumer());
            }
        });
    }

    private void initializeDropdown(){
        ArrayList<String>arrayPole=new ArrayList<>();
        for (int i = 0; i < Global.getGeoJsonData().size(); i++) {
            String layarname=Global.getGeoJsonData().get(i).getLayerName();
            if(layarname.equalsIgnoreCase("Pole")) {
                String jsondata = Global.getGeoJsonData().get(i).getGeoJson();
                try {

                    String data = jsondata.substring(jsondata.indexOf("["));
                    JSONArray json = new JSONArray(data);
                    for (int j = 0; j < json.length(); j++) {
                        JSONObject jsonObject1 = json.getJSONObject(j);
                        String property= jsonObject1.getString("properties");
                        JSONObject jsonObject12=new JSONObject(property);
                        String ninid= jsonObject12.getString("nin_id");
                        arrayPole.add(ninid);

                    }
                }catch(JSONException e)
                {
                }
            }
        }

        ArrayAdapter<CharSequence> adapterCateogry =
                ArrayAdapter.createFromResource(getActivity(), R.array.category, android.R.layout.simple_spinner_item);
        adapterCateogry.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.spinCategory.setAdapter(adapterCateogry);

        ArrayAdapter<CharSequence> adapterSupplyVoltage =
                ArrayAdapter.createFromResource(getActivity(), R.array.SupplyVoltage, android.R.layout.simple_spinner_item);
        adapterSupplyVoltage.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.spinSupplyVoltage.setAdapter(adapterSupplyVoltage);

        ArrayAdapter<CharSequence> adapterNoOfPhases =
                ArrayAdapter.createFromResource(getActivity(), R.array.NoOfPhases, android.R.layout.simple_spinner_item);
        adapterNoOfPhases.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.spinNoOfPhases.setAdapter(adapterNoOfPhases);

        ArrayAdapter<CharSequence> adapterConnectionStatus =
                ArrayAdapter.createFromResource(getActivity(), R.array.ConnectionStatus, android.R.layout.simple_spinner_item);
        adapterConnectionStatus.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.spinConnectionStatus.setAdapter(adapterConnectionStatus);

        ArrayAdapter<String> adapterPoleFPSPGPSID =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayPole);
       /* ArrayAdapter<String> adapterPoleFPSPGPSID =
                ArrayAdapter.createFromResource(getActivity(), arrayPole, android.R.layout.simple_spinner_item);*/
        adapterPoleFPSPGPSID.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.spinPoleFPSPGPSID.setAdapter(adapterPoleFPSPGPSID);

        ArrayAdapter<CharSequence> adapterPhysicallocationofmeter  =
                ArrayAdapter.createFromResource(getActivity(), R.array.Physicallocationofmeter, android.R.layout.simple_spinner_item);
        adapterPhysicallocationofmeter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.spinPhysicallocationofmeter.setAdapter(adapterPhysicallocationofmeter);

        ArrayAdapter<CharSequence> adapterViewingGlass =
                ArrayAdapter.createFromResource(getActivity(), R.array.ViewingGlass, android.R.layout.simple_spinner_item);
        adapterViewingGlass.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.spinViewingGlass.setAdapter(adapterViewingGlass);



        ArrayAdapter<CharSequence> adapterTypeofMeter =
                ArrayAdapter.createFromResource(getActivity(), R.array.TypeofMeter, android.R.layout.simple_spinner_item);
        adapterTypeofMeter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.spinTypeofMeter.setAdapter(adapterTypeofMeter);

        ArrayAdapter<CharSequence> adapterSeal =
                ArrayAdapter.createFromResource(getActivity(), R.array.Seal, android.R.layout.simple_spinner_item);
        adapterSeal.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.spinSeal.setAdapter(adapterSeal);

        ArrayAdapter<CharSequence> adapterPolehavingmultiplefeeder  =
                ArrayAdapter.createFromResource(getActivity(), R.array.Polehavingmultiplefeeder, android.R.layout.simple_spinner_item);
        adapterPolehavingmultiplefeeder.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.spinPolehavingmultiplefeeder.setAdapter(adapterPolehavingmultiplefeeder);

        ArrayAdapter<CharSequence> adapterRuralurban  =
                ArrayAdapter.createFromResource(getActivity(), R.array.ruralurban, android.R.layout.simple_spinner_item);
        adapterRuralurban.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.spinRuralurban.setAdapter(adapterRuralurban);
    }

    private PostConsumerCreate getPostConsumer(){

        PostConsumerCreate consumerObject = new PostConsumerCreate();
        consumerObject.setNin_id(Global.getSelectedAssignedTask().getDtr_nin());
        consumerObject.setBld_id(Global.getStringSharedRef(getActivity(), AppUtils.ACTION_BUILDING_ID));
        consumerObject.setDtr_name(Global.getSelectedAssignedTask().getDt_name());
        consumerObject.setSubstation_name(Global.getSelectedAssignedTask().getSubstation11());
        consumerObject.setCus_no(binding.editcustomernoexiting.getText().toString());
        consumerObject.setCus_name(binding.editCustomername.getText().toString());
        consumerObject.setFat_name(binding.editFathername.getText().toString());
        consumerObject.setOcc_name(binding.editOccupantName.getText().toString());
        consumerObject.setTower_no(binding.editTowerPlotFlatHouseShopNo.getText().toString());
        consumerObject.setBld_name(binding.editBuildingApartmentName.getText().toString());
        consumerObject.setBloc_no(binding.editBlockPocketNo.getText().toString());
        consumerObject.setSec_name(binding.editSectorColonyName.getText().toString());
        consumerObject.setLoc_name(binding.editLocalityName.getText().toString());
        consumerObject.setRural(binding.spinRuralurban.getSelectedItem().toString());
        consumerObject.setCity_name(binding.editCityVillageName.getText().toString());
        consumerObject.setPincode(binding.editPincode.getText().toString());
        consumerObject.setSan_load(binding.editSanctionedLoad.getText().toString());
        consumerObject.setCategory(binding.spinCategory.getSelectedItem().toString());
        consumerObject.setSup_volt(binding.spinSupplyVoltage.getSelectedItem().toString());
        consumerObject.setNo_of_phase(binding.spinNoOfPhases.getSelectedItem().toString());
        consumerObject.setCon_status(binding.spinConnectionStatus.getSelectedItem().toString());
        consumerObject.setMet_no(binding.editmeterno.getText().toString());
        consumerObject.setDtr_id(Global.getSelectedAssignedTask().getDTRID());
        consumerObject.setFdr_name(Global.getSelectedAssignedTask().getFeeder11());
        consumerObject.setStat_name(binding.editStationName.getText().toString());
        consumerObject.setPole_id(binding.spinPoleFPSPGPSID.getSelectedItem().toString());
        consumerObject.setLeft_cus_no(binding.editLeftRightsideHouseCustomerNo.getText().toString());
        consumerObject.setMet_lo(binding.spinPhysicallocationofmeter.getSelectedItem().toString());
        consumerObject.setHig_mount(binding.editHeightofMounting.getText().toString());
        consumerObject.setView_glass(binding.spinViewingGlass.getSelectedItem().toString());
        consumerObject.setMet_type(binding.spinTypeofMeter.getSelectedItem().toString());
        consumerObject.setMake_class(binding.editMakeandClass.getText().toString());
        consumerObject.setSeal_no(binding.editSealNo.getText().toString());
        consumerObject.setSeal(binding.spinSeal.getSelectedItem().toString());
        consumerObject.setPole_multi_fdr(binding.spinPolehavingmultiplefeeder.getSelectedItem().toString());
        consumerObject.setX(String.valueOf(AppUtils.selectedLatLon.longitude));
        consumerObject.setY(String.valueOf(AppUtils.selectedLatLon.latitude));
        consumerObject.setCreatedBy(Global.getUsername());
        consumerObject.setCreatedDate(Global.getCurrentDateAndTime());
        consumerObject.setTown(Global.getSelectedAssignedTask().getTown());
        String base64Image = getImageBaseString(extension);
        if (extension != null && extension != "" && extension.length() > 0) {
            consumerObject.setImg_meter(base64Image);
            consumerObject.setImg_meter_ext(extension);
        } else {
            consumerObject.setImg_meter("NA");
            consumerObject.setImg_meter_ext("NA");
        }
        String base64Image2 = getImageBaseStringForBilling(extension);
        if (extension != null && extension != "" && extension.length() > 0) {
            consumerObject.setImg_bill(base64Image2);
            consumerObject.setImg_bill_ext(extension);
        } else {
            consumerObject.setImg_bill("NA");
            consumerObject.setImg_bill_ext("NA");
        }


        return consumerObject;
    }

    @Override
    public void onSuccess() {
        LocationInfo info = new LocationInfo();
        info.setLocation(AppUtils.selectedLatLon);
        info.setTitle("Test from c");
        info.setType("C");
        Global.lstPoleLatLngs.add(info);
        binding.editbuildingid.setText(Global.getStringSharedRef(getActivity(), AppUtils.ACTION_BUILDING_ID));

    }
    @Override
    public void onSaveconsumer() {
        Navigation.findNavController(binding.getRoot()).navigate(ConsumerSurveyFormFragmentDirections.actionConsumerSurveyFormFragment());
    }

    @Override
    public void onEmpty(String Msg) {

    }

    @Override
    public void onFailure(String Msg) {

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), AppUtils.ALL_PERMISSIONS, AppUtils.MY_PERMISSIONS_REQUEST);
    }

    public void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createMediaFile(AppUtils.MEDIA_IMAGE, AppUtils.IMAGE_EXTN);
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getContext(),
                        AppUtils.AUTHORITY,
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, CAMERA);
            }
        }

    }
    public void takePhotoForBillingFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createMediaFile(AppUtils.MEDIA_IMAGE, AppUtils.IMAGE_EXTN);
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getContext(),
                        AppUtils.AUTHORITY,
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, CAMERABILL);
            }
        }

    }
    private File createMediaFile(String type, String extension) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = type + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                extension,         /* suffix */
                storageDir      /* directory */
        );


        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        if (requestCode == GALLERY) {
            if (data != null) {
                contentURI = data.getData();
                /*try {
                    isCamera = false;
                    Intent crop = new Intent(getActivity(), ImageCropActivity.class);
                    crop.putExtra("uri", contentURI.toString());
                    startActivityForResult(crop, GALLERY_CROP);
                } catch (Exception ex) {

                }*/

                String type = mime.getExtensionFromMimeType(cR.getType(contentURI));
                try {
                    thumbnail = (Bitmap) Global.compressImage(contentURI, "gallery_image", getActivity());
                } catch (Exception ex) {

                }
                addImage(contentURI, type);
            }
        } else if (requestCode == CAMERA) {
            try {
                thumbnail = (Bitmap) Global.compressImage(Uri.parse(mCurrentPhotoPath), "camera_image", getActivity());
            } catch (Exception ex) {

            }

            /*Intent crop = new Intent(getActivity(), ImageCropActivity.class);
            crop.putExtra("uri", photoURI);

            startActivityForResult(crop, GALLERY_CROP);*/

            String type = mime.getExtensionFromMimeType(cR.getType(photoURI));

            addImage(photoURI, type);

        } else if (requestCode == CAMERABILL) {
            try {
                thumbnail1 = (Bitmap) Global.compressImage(Uri.parse(mCurrentPhotoPath), "camera_image", getActivity());
            } catch (Exception ex) {

            }

            /*Intent crop = new Intent(getActivity(), ImageCropActivity.class);
            crop.putExtra("uri", photoURI);

            startActivityForResult(crop, GALLERY_CROP);*/

            String type = mime.getExtensionFromMimeType(cR.getType(photoURI));

            addBillImage(photoURI, type);

        } else if (requestCode == GALLERY_CROP && resultCode == -1) {
         //   galleryURI = Uri.parse(data.getExtras().getString("uri"));

        }
    }

    private void addImage(Uri imgUri, String ext) {
        extension = ext;
        //binding.imgCamera.setImageURI(imgUri);
        Bitmap bitmap = Bitmap.createScaledBitmap(thumbnail, 300, 300, true);
        binding.imgCamera.setImageBitmap(bitmap);
        binding.imgCamera.setScaleType(ImageView.ScaleType.FIT_XY);


    }
    private void addBillImage(Uri imgUri, String ext) {
        extension = ext;
        //binding.imgCamera.setImageURI(imgUri);
        Bitmap bitmap = Bitmap.createScaledBitmap(thumbnail1, 300, 300, true);
        binding.imgCamerabill.setImageBitmap(bitmap);
        binding.imgCamerabill.setScaleType(ImageView.ScaleType.FIT_XY);


    }

    private String getImageBaseString(String extension){
        return Global.encodeImage(((BitmapDrawable) binding.imgCamera.getDrawable()).getBitmap(), extension);
    }
    private String getImageBaseStringForBilling(String extension){
        return Global.encodeImage(((BitmapDrawable) binding.imgCamerabill.getDrawable()).getBitmap(), extension);
    }
}