include $(CLEAR_VARS)
# ./configure --build=x86_64-pc-linux-gnu --host=arm-linux-eabi
# -------------------
# As of 2019-07-01
# -------------------
# Excluded files:
# - none
# Define to 1 if you have localeconv
# -> set to 0 (comment out in src/proj_config.h after ./configure)
# -------------------
LOCAL_MODULE    := proj

# Enable c++11 extentions in source code for PROJ Version >= 6.0
# - should not be used by GEOS
LOCAL_CPP_FEATURES := -std=c++11

LOCAL_C_INCLUDES := \
 $(SQLITE_PATH) \
 $(PROJ_PATH)/src \
 $(PROJ_PATH)/include

# LOCAL_LDLIBS is always ignored for static libraries
# LOCAL_LDLIBS := -lm
LOCAL_SRC_FILES := \
 $(PROJ_PATH)/src/iso19111/static.cpp \
 $(PROJ_PATH)/src/iso19111/util.cpp \
 $(PROJ_PATH)/src/iso19111/metadata.cpp \
 $(PROJ_PATH)/src/iso19111/common.cpp \
 $(PROJ_PATH)/src/iso19111/crs.cpp \
 $(PROJ_PATH)/src/iso19111/datum.cpp \
 $(PROJ_PATH)/src/iso19111/coordinatesystem.cpp \
 $(PROJ_PATH)/src/iso19111/coordinateoperation.cpp \
 $(PROJ_PATH)/src/iso19111/io.cpp \
 $(PROJ_PATH)/src/iso19111/internal.cpp \
 $(PROJ_PATH)/src/iso19111/factory.cpp \
 $(PROJ_PATH)/src/iso19111/c_api.cpp \
 $(PROJ_PATH)/src/projections/aeqd.cpp \
 $(PROJ_PATH)/src/projections/gnom.cpp \
 $(PROJ_PATH)/src/projections/laea.cpp \
 $(PROJ_PATH)/src/projections/mod_ster.cpp \
 $(PROJ_PATH)/src/projections/nsper.cpp \
 $(PROJ_PATH)/src/projections/nzmg.cpp \
 $(PROJ_PATH)/src/projections/ortho.cpp \
 $(PROJ_PATH)/src/projections/stere.cpp \
 $(PROJ_PATH)/src/projections/sterea.cpp \
 $(PROJ_PATH)/src/projections/aea.cpp \
 $(PROJ_PATH)/src/projections/bipc.cpp \
 $(PROJ_PATH)/src/projections/bonne.cpp \
 $(PROJ_PATH)/src/projections/eqdc.cpp \
 $(PROJ_PATH)/src/projections/isea.cpp \
 $(PROJ_PATH)/src/projections/ccon.cpp \
 $(PROJ_PATH)/src/projections/imw_p.cpp \
 $(PROJ_PATH)/src/projections/krovak.cpp \
 $(PROJ_PATH)/src/projections/lcc.cpp \
 $(PROJ_PATH)/src/projections/poly.cpp \
 $(PROJ_PATH)/src/projections/rpoly.cpp \
 $(PROJ_PATH)/src/projections/sconics.cpp \
 $(PROJ_PATH)/src/projections/rouss.cpp \
 $(PROJ_PATH)/src/projections/cass.cpp \
 $(PROJ_PATH)/src/projections/cc.cpp \
 $(PROJ_PATH)/src/projections/cea.cpp \
 $(PROJ_PATH)/src/projections/eqc.cpp \
 $(PROJ_PATH)/src/projections/gall.cpp \
 $(PROJ_PATH)/src/projections/labrd.cpp \
 $(PROJ_PATH)/src/projections/lsat.cpp \
 $(PROJ_PATH)/src/projections/misrsom.cpp \
 $(PROJ_PATH)/src/projections/merc.cpp \
 $(PROJ_PATH)/src/projections/mill.cpp \
 $(PROJ_PATH)/src/projections/ocea.cpp \
 $(PROJ_PATH)/src/projections/omerc.cpp \
 $(PROJ_PATH)/src/projections/somerc.cpp \
 $(PROJ_PATH)/src/projections/tcc.cpp \
 $(PROJ_PATH)/src/projections/tcea.cpp \
 $(PROJ_PATH)/src/projections/times.cpp \
 $(PROJ_PATH)/src/projections/tmerc.cpp \
 $(PROJ_PATH)/src/projections/tobmerc.cpp \
 $(PROJ_PATH)/src/projections/airy.cpp \
 $(PROJ_PATH)/src/projections/aitoff.cpp \
 $(PROJ_PATH)/src/projections/august.cpp \
 $(PROJ_PATH)/src/projections/bacon.cpp \
 $(PROJ_PATH)/src/projections/bertin1953.cpp \
 $(PROJ_PATH)/src/projections/chamb.cpp \
 $(PROJ_PATH)/src/projections/hammer.cpp \
 $(PROJ_PATH)/src/projections/lagrng.cpp \
 $(PROJ_PATH)/src/projections/larr.cpp \
 $(PROJ_PATH)/src/projections/lask.cpp \
 $(PROJ_PATH)/src/projections/latlong.cpp \
 $(PROJ_PATH)/src/projections/nicol.cpp \
 $(PROJ_PATH)/src/projections/ob_tran.cpp \
 $(PROJ_PATH)/src/projections/oea.cpp \
 $(PROJ_PATH)/src/projections/tpeqd.cpp \
 $(PROJ_PATH)/src/projections/vandg.cpp \
 $(PROJ_PATH)/src/projections/vandg2.cpp \
 $(PROJ_PATH)/src/projections/vandg4.cpp \
 $(PROJ_PATH)/src/projections/wag7.cpp \
 $(PROJ_PATH)/src/projections/lcca.cpp \
 $(PROJ_PATH)/src/projections/geos.cpp \
 $(PROJ_PATH)/src/projections/boggs.cpp \
 $(PROJ_PATH)/src/projections/collg.cpp \
 $(PROJ_PATH)/src/projections/comill.cpp \
 $(PROJ_PATH)/src/projections/crast.cpp \
 $(PROJ_PATH)/src/projections/denoy.cpp \
 $(PROJ_PATH)/src/projections/eck1.cpp \
 $(PROJ_PATH)/src/projections/eck2.cpp \
 $(PROJ_PATH)/src/projections/eck3.cpp \
 $(PROJ_PATH)/src/projections/eck4.cpp \
 $(PROJ_PATH)/src/projections/eck5.cpp \
 $(PROJ_PATH)/src/projections/fahey.cpp \
 $(PROJ_PATH)/src/projections/fouc_s.cpp \
 $(PROJ_PATH)/src/projections/gins8.cpp \
 $(PROJ_PATH)/src/projections/gstmerc.cpp \
 $(PROJ_PATH)/src/projections/gn_sinu.cpp \
 $(PROJ_PATH)/src/projections/goode.cpp \
 $(PROJ_PATH)/src/projections/igh.cpp \
 $(PROJ_PATH)/src/projections/hatano.cpp \
 $(PROJ_PATH)/src/projections/loxim.cpp \
 $(PROJ_PATH)/src/projections/mbt_fps.cpp \
 $(PROJ_PATH)/src/projections/mbtfpp.cpp \
 $(PROJ_PATH)/src/projections/mbtfpq.cpp \
 $(PROJ_PATH)/src/projections/moll.cpp \
 $(PROJ_PATH)/src/projections/nell.cpp \
 $(PROJ_PATH)/src/projections/nell_h.cpp \
 $(PROJ_PATH)/src/projections/patterson.cpp \
 $(PROJ_PATH)/src/projections/putp2.cpp \
 $(PROJ_PATH)/src/projections/putp3.cpp \
 $(PROJ_PATH)/src/projections/putp4p.cpp \
 $(PROJ_PATH)/src/projections/putp5.cpp \
 $(PROJ_PATH)/src/projections/putp6.cpp \
 $(PROJ_PATH)/src/projections/qsc.cpp \
 $(PROJ_PATH)/src/projections/robin.cpp \
 $(PROJ_PATH)/src/projections/sch.cpp \
 $(PROJ_PATH)/src/projections/sts.cpp \
 $(PROJ_PATH)/src/projections/urm5.cpp \
 $(PROJ_PATH)/src/projections/urmfps.cpp \
 $(PROJ_PATH)/src/projections/wag2.cpp \
 $(PROJ_PATH)/src/projections/wag3.cpp \
 $(PROJ_PATH)/src/projections/wink1.cpp \
 $(PROJ_PATH)/src/projections/wink2.cpp \
 $(PROJ_PATH)/src/projections/healpix.cpp \
 $(PROJ_PATH)/src/projections/natearth.cpp \
 $(PROJ_PATH)/src/projections/natearth2.cpp \
 $(PROJ_PATH)/src/projections/calcofi.cpp \
 $(PROJ_PATH)/src/projections/eqearth.cpp \
 $(PROJ_PATH)/src/conversions/axisswap.cpp \
 $(PROJ_PATH)/src/conversions/cart.cpp \
 $(PROJ_PATH)/src/conversions/geoc.cpp \
 $(PROJ_PATH)/src/conversions/geocent.cpp \
 $(PROJ_PATH)/src/conversions/noop.cpp \
 $(PROJ_PATH)/src/conversions/unitconvert.cpp \
 $(PROJ_PATH)/src/transformations/affine.cpp \
 $(PROJ_PATH)/src/transformations/deformation.cpp \
 $(PROJ_PATH)/src/transformations/helmert.cpp \
 $(PROJ_PATH)/src/transformations/hgridshift.cpp \
 $(PROJ_PATH)/src/transformations/horner.cpp \
 $(PROJ_PATH)/src/transformations/molodensky.cpp \
 $(PROJ_PATH)/src/transformations/vgridshift.cpp \
 $(PROJ_PATH)/src/aasincos.cpp \
 $(PROJ_PATH)/src/adjlon.cpp \
 $(PROJ_PATH)/src/dmstor.cpp \
 $(PROJ_PATH)/src/auth.cpp \
 $(PROJ_PATH)/src/deriv.cpp \
 $(PROJ_PATH)/src/ell_set.cpp \
 $(PROJ_PATH)/src/ellps.cpp \
 $(PROJ_PATH)/src/errno.cpp \
 $(PROJ_PATH)/src/factors.cpp \
 $(PROJ_PATH)/src/fwd.cpp \
 $(PROJ_PATH)/src/init.cpp \
 $(PROJ_PATH)/src/inv.cpp \
 $(PROJ_PATH)/src/list.cpp \
 $(PROJ_PATH)/src/malloc.cpp \
 $(PROJ_PATH)/src/mlfn.cpp \
 $(PROJ_PATH)/src/msfn.cpp \
 $(PROJ_PATH)/src/proj_mdist.cpp \
 $(PROJ_PATH)/src/open_lib.cpp \
 $(PROJ_PATH)/src/param.cpp \
 $(PROJ_PATH)/src/phi2.cpp \
 $(PROJ_PATH)/src/pr_list.cpp \
 $(PROJ_PATH)/src/qsfn.cpp \
 $(PROJ_PATH)/src/strerrno.cpp \
 $(PROJ_PATH)/src/tsfn.cpp \
 $(PROJ_PATH)/src/units.cpp \
 $(PROJ_PATH)/src/ctx.cpp \
 $(PROJ_PATH)/src/log.cpp \
 $(PROJ_PATH)/src/zpoly1.cpp \
 $(PROJ_PATH)/src/rtodms.cpp \
 $(PROJ_PATH)/src/release.cpp \
 $(PROJ_PATH)/src/gauss.cpp \
 $(PROJ_PATH)/src/fileapi.cpp \
 $(PROJ_PATH)/src/gc_reader.cpp \
 $(PROJ_PATH)/src/gridcatalog.cpp \
 $(PROJ_PATH)/src/nad_cvt.cpp \
 $(PROJ_PATH)/src/nad_init.cpp \
 $(PROJ_PATH)/src/nad_intr.cpp \
 $(PROJ_PATH)/src/apply_gridshift.cpp \
 $(PROJ_PATH)/src/datums.cpp \
 $(PROJ_PATH)/src/datum_set.cpp \
 $(PROJ_PATH)/src/transform.cpp \
 $(PROJ_PATH)/src/geocent.cpp \
 $(PROJ_PATH)/src/utils.cpp \
 $(PROJ_PATH)/src/gridinfo.cpp \
 $(PROJ_PATH)/src/gridlist.cpp \
 $(PROJ_PATH)/src/jniproj.cpp \
 $(PROJ_PATH)/src/mutex.cpp \
 $(PROJ_PATH)/src/initcache.cpp \
 $(PROJ_PATH)/src/apply_vgridshift.cpp \
 $(PROJ_PATH)/src/geodesic.c \
 $(PROJ_PATH)/src/strtod.cpp \
 $(PROJ_PATH)/src/math.cpp \
 $(PROJ_PATH)/src/4D_api.cpp \
 $(PROJ_PATH)/src/pipeline.cpp \
 $(PROJ_PATH)/src/internal.cpp \
 $(PROJ_PATH)/src/wkt_parser.cpp \
 $(PROJ_PATH)/src/wkt1_parser.cpp \
 $(PROJ_PATH)/src/wkt1_generated_parser.c \
 $(PROJ_PATH)/src/wkt2_parser.cpp \
 $(PROJ_PATH)/src/wkt2_generated_parser.c
include $(BUILD_STATIC_LIBRARY)
