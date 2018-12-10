/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cydeep.imageedit.imageEdit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import com.cydeep.imageedit.R;
import com.cydeep.imageedit.imageEdit.filter.IF1977Filter;
import com.cydeep.imageedit.imageEdit.filter.IFAmaroFilter;
import com.cydeep.imageedit.imageEdit.filter.IFBrannanFilter;
import com.cydeep.imageedit.imageEdit.filter.IFEarlybirdFilter;
import com.cydeep.imageedit.imageEdit.filter.IFHefeFilter;
import com.cydeep.imageedit.imageEdit.filter.IFHudsonFilter;
import com.cydeep.imageedit.imageEdit.filter.IFInkwellFilter;
import com.cydeep.imageedit.imageEdit.filter.IFLomoFilter;
import com.cydeep.imageedit.imageEdit.filter.IFLordKelvinFilter;
import com.cydeep.imageedit.imageEdit.filter.IFNashvilleFilter;
import com.cydeep.imageedit.imageEdit.filter.IFRiseFilter;
import com.cydeep.imageedit.imageEdit.filter.IFSierraFilter;
import com.cydeep.imageedit.imageEdit.filter.IFSutroFilter;
import com.cydeep.imageedit.imageEdit.filter.IFToasterFilter;
import com.cydeep.imageedit.imageEdit.filter.IFValenciaFilter;
import com.cydeep.imageedit.imageEdit.filter.IFWaldenFilter;
import com.cydeep.imageedit.imageEdit.filter.IFXprollFilter;
import com.cydeep.imageedit.util.LogUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage3x3ConvolutionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImage3x3TextureSamplingFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageAddBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageChromaKeyBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBurnBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorDodgeBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDarkenBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDifferenceBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDirectionalSobelEdgeDetectionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDissolveBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDivideBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExclusionBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHardLightBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLightenBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLinearBurnBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLookupFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLuminosityBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMonochromeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMultiplyBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageNormalBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOpacityFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOverlayBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageScreenBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;
import jp.co.cyberagent.android.gpuimage.GPUImageSoftLightBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSourceOverBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSubtractBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageTwoInputFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;


public class GPUImageFilterTools {
    public int resId;

    public static void showDialog(final Context context,
            final OnGpuImageFilterChosenListener listener) {
        final List<String> filters = new ArrayList<>();
        filters.add(FilterType.DEFAULT);
        filters.add(GPUImageFilterTools.FilterType.I_1977);
        filters.add(GPUImageFilterTools.FilterType.I_AMARO);
        filters.add(GPUImageFilterTools.FilterType.I_BRANNAN);
        filters.add(GPUImageFilterTools.FilterType.I_EARLYBIRD);
        filters.add(GPUImageFilterTools.FilterType.I_HEFE);
        filters.add(GPUImageFilterTools.FilterType.I_HUDSON);
        filters.add(GPUImageFilterTools.FilterType.I_INKWELL);
        filters.add(GPUImageFilterTools.FilterType.I_LOMO);
        filters.add(GPUImageFilterTools.FilterType.I_LORDKELVIN);
        filters.add(GPUImageFilterTools.FilterType.I_NASHVILLE);
        filters.add(GPUImageFilterTools.FilterType.I_NASHVILLE);
        filters.add(GPUImageFilterTools.FilterType.I_SIERRA);
        filters.add(GPUImageFilterTools.FilterType.I_SUTRO);
        filters.add(GPUImageFilterTools.FilterType.I_TOASTER);
        filters.add(GPUImageFilterTools.FilterType.I_VALENCIA);
        filters.add(GPUImageFilterTools.FilterType.I_WALDEN);
        filters.add(GPUImageFilterTools.FilterType.I_XPROII);
        filters.add(GPUImageFilterTools.FilterType.CONTRAST);
        filters.add(GPUImageFilterTools.FilterType.BRIGHTNESS);
        filters.add(GPUImageFilterTools.FilterType.SEPIA);
        filters.add(GPUImageFilterTools.FilterType.VIGNETTE);
        filters.add(GPUImageFilterTools.FilterType.TONE_CURVE);
        filters.add(GPUImageFilterTools.FilterType.LOOKUP_AMATORKA);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose a filter");
        builder.setItems(filters.toArray(new String[filters.size()]),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int item) {
                        listener.onGpuImageFilterChosenListener(
                                createFilterForType(context, filters.get(item)));
                    }
                });
        builder.create().show();
    }

    public static GPUImageFilter createFilterForType(final Context context, final String type) {
        switch (type) {
            case FilterType.DEFAULT:
                return new GPUImageFilter();
            case FilterType.CONTRAST:
                return new GPUImageContrastFilter(2.0f);
            case FilterType.GAMMA:
                return new GPUImageGammaFilter(2.0f);
            case FilterType.INVERT:
                return new GPUImageColorInvertFilter();
            case FilterType.PIXELATION:
                return new GPUImagePixelationFilter();
            case FilterType.HUE:
                return new GPUImageHueFilter(90.0f);
            case FilterType.BRIGHTNESS:
                return new GPUImageBrightnessFilter(1.5f);
            case FilterType.GRAYSCALE:
                return new GPUImageGrayscaleFilter();
            case FilterType.SEPIA:
                return new GPUImageSepiaFilter();
            case FilterType.SHARPEN:
                GPUImageSharpenFilter sharpness = new GPUImageSharpenFilter();
                sharpness.setSharpness(2.0f);
                return sharpness;
            case FilterType.SOBEL_EDGE_DETECTION:
                return new GPUImageSobelEdgeDetection();
            case FilterType.THREE_X_THREE_CONVOLUTION:
                GPUImage3x3ConvolutionFilter convolution = new GPUImage3x3ConvolutionFilter();
                convolution.setConvolutionKernel(new float[] {
                        -1.0f, 0.0f, 1.0f,
                        -2.0f, 0.0f, 2.0f,
                        -1.0f, 0.0f, 1.0f
                });
                return convolution;
            case FilterType.EMBOSS:
                return new GPUImageEmbossFilter();
            case FilterType.POSTERIZE:
                return new GPUImagePosterizeFilter();
            case FilterType.FILTER_GROUP:
                List<GPUImageFilter> filters = new LinkedList<GPUImageFilter>();
                filters.add(new GPUImageContrastFilter());
                filters.add(new GPUImageDirectionalSobelEdgeDetectionFilter());
                filters.add(new GPUImageGrayscaleFilter());
                return new GPUImageFilterGroup(filters);
            case FilterType.SATURATION:
                return new GPUImageSaturationFilter(1.0f);
            case FilterType.EXPOSURE:
                return new GPUImageExposureFilter(0.0f);
            case FilterType.HIGHLIGHT_SHADOW:
            	return new GPUImageHighlightShadowFilter(0.0f, 1.0f);
            case FilterType.MONOCHROME:
            	return new GPUImageMonochromeFilter(1.0f, new float[]{0.6f, 0.45f, 0.3f, 1.0f});
            case FilterType.OPACITY:
                return new GPUImageOpacityFilter(1.0f);
            case FilterType.RGB:
                return new GPUImageRGBFilter(1.0f, 1.0f, 1.0f);
            case FilterType.WHITE_BALANCE:
                return new GPUImageWhiteBalanceFilter(5000.0f, 0.0f);
            case FilterType.VIGNETTE:
                PointF centerPoint = new PointF();
                centerPoint.x = 0.5f;
                centerPoint.y = 0.5f;
                return new GPUImageVignetteFilter(centerPoint, new float[] {0.0f, 0.0f, 0.0f}, 0.3f, 0.75f);
            case FilterType.TONE_CURVE:
                GPUImageToneCurveFilter toneCurveFilter = new GPUImageToneCurveFilter();
                toneCurveFilter.setFromCurveFileInputStream(
                        context.getResources().openRawResource(R.raw.tone_cuver_sample));
                return toneCurveFilter;
            case FilterType.BLEND_DIFFERENCE:
                return createBlendFilter(context, GPUImageDifferenceBlendFilter.class);
            case FilterType.BLEND_SOURCE_OVER:
                return createBlendFilter(context, GPUImageSourceOverBlendFilter.class);
            case FilterType.BLEND_COLOR_BURN:
                return createBlendFilter(context, GPUImageColorBurnBlendFilter.class);
            case FilterType.BLEND_COLOR_DODGE:
                return createBlendFilter(context, GPUImageColorDodgeBlendFilter.class);
            case FilterType.BLEND_DARKEN:
                return createBlendFilter(context, GPUImageDarkenBlendFilter.class);
            case FilterType.BLEND_DISSOLVE:
                return createBlendFilter(context, GPUImageDissolveBlendFilter.class);
            case FilterType.BLEND_EXCLUSION:
                return createBlendFilter(context, GPUImageExclusionBlendFilter.class);


            case FilterType.BLEND_HARD_LIGHT:
                return createBlendFilter(context, GPUImageHardLightBlendFilter.class);
            case FilterType.BLEND_LIGHTEN:
                return createBlendFilter(context, GPUImageLightenBlendFilter.class);
            case FilterType.BLEND_ADD:
                return createBlendFilter(context, GPUImageAddBlendFilter.class);
            case FilterType.BLEND_DIVIDE:
                return createBlendFilter(context, GPUImageDivideBlendFilter.class);
            case FilterType.BLEND_MULTIPLY:
                return createBlendFilter(context, GPUImageMultiplyBlendFilter.class);
            case FilterType.BLEND_OVERLAY:
                return createBlendFilter(context, GPUImageOverlayBlendFilter.class);
            case FilterType.BLEND_SCREEN:
                return createBlendFilter(context, GPUImageScreenBlendFilter.class);
            case FilterType.BLEND_ALPHA:
                return createBlendFilter(context, GPUImageAlphaBlendFilter.class);
            case FilterType.BLEND_COLOR:
                return createBlendFilter(context, GPUImageColorBlendFilter.class);
            case FilterType.BLEND_HUE:
                return createBlendFilter(context, GPUImageHueBlendFilter.class);
            case FilterType.BLEND_SATURATION:
                return createBlendFilter(context, GPUImageSaturationBlendFilter.class);
            case FilterType.BLEND_LUMINOSITY:
                return createBlendFilter(context, GPUImageLuminosityBlendFilter.class);
            case FilterType.BLEND_LINEAR_BURN:
                return createBlendFilter(context, GPUImageLinearBurnBlendFilter.class);
            case FilterType.BLEND_SOFT_LIGHT:
                return createBlendFilter(context, GPUImageSoftLightBlendFilter.class);
            case FilterType.BLEND_SUBTRACT:
                return createBlendFilter(context, GPUImageSubtractBlendFilter.class);
            case FilterType.BLEND_CHROMA_KEY:
                return createBlendFilter(context, GPUImageChromaKeyBlendFilter.class);
            case FilterType.BLEND_NORMAL:
                return createBlendFilter(context, GPUImageNormalBlendFilter.class);

            case FilterType.LOOKUP_AMATORKA:
                GPUImageLookupFilter amatorka = new GPUImageLookupFilter();
                amatorka.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.lookup_amatorka));
                return amatorka;

            case FilterType.I_1977:
                return new IF1977Filter(context);
            case FilterType.I_AMARO:
                return new IFAmaroFilter(context);
            case FilterType.I_BRANNAN:
                return new IFBrannanFilter(context);
            case FilterType.I_EARLYBIRD:
                return new IFEarlybirdFilter(context);
            case FilterType.I_HEFE:
                return new IFHefeFilter(context);
            case FilterType.I_HUDSON:
                return new IFHudsonFilter(context);
            case FilterType.I_INKWELL:
                return new IFInkwellFilter(context);
            case FilterType.I_LOMO:
                return new IFLomoFilter(context);
            case FilterType.I_LORDKELVIN:
                return new IFLordKelvinFilter(context);
            case FilterType.I_NASHVILLE:
                return new IFNashvilleFilter(context);
            case FilterType.I_RISE:
                return new IFRiseFilter(context);
            case FilterType.I_SIERRA:
                return new IFSierraFilter(context);
            case FilterType.I_SUTRO:
                return new IFSutroFilter(context);
            case FilterType.I_TOASTER:
                return new IFToasterFilter(context);
            case FilterType.I_VALENCIA:
                return new IFValenciaFilter(context);
            case FilterType.I_WALDEN:
                return new IFWaldenFilter(context);
            case FilterType.I_XPROII:
                return new IFXprollFilter(context);
            default:
                LogUtils.i("filter",type);
                throw new IllegalStateException("No filter of that type!");
        }

    }

    private static GPUImageFilter createBlendFilter(Context context, Class<? extends GPUImageTwoInputFilter> filterClass) {
        try {
            GPUImageTwoInputFilter filter = filterClass.newInstance();
            filter.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
            return filter;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface OnGpuImageFilterChosenListener {
        void onGpuImageFilterChosenListener(GPUImageFilter filter);
    }

    public static class FilterType {
        public static final String CONTRAST = "0";
        public static final String GRAYSCALE = "1";
        public static final String SHARPEN = "2";
        public static final String SEPIA = "3";
        public static final String SOBEL_EDGE_DETECTION = "4";
        public static final String THREE_X_THREE_CONVOLUTION = "5";
        public static final String FILTER_GROUP = "6";
        public static final String EMBOSS = "7";
        public static final String POSTERIZE = "8";
        public static final String GAMMA = "9";
        public static final String BRIGHTNESS = "10";
        public static final String INVERT = "11";
        public static final String HUE = "12";
        public static final String PIXELATION = "13";
        public static final String SATURATION = "14";
        public static final String EXPOSURE = "15";
        public static final String HIGHLIGHT_SHADOW = "16";
        public static final String MONOCHROME = "17";
        public static final String OPACITY = "18";
        public static final String RGB = "19";
        public static final String WHITE_BALANCE = "20";
        public static final String VIGNETTE = "21";
        public static final String TONE_CURVE = "22";
        public static final String BLEND_COLOR_BURN ="23";
        public static final String BLEND_COLOR_DODGE = "24";
        public static final String BLEND_DARKEN = "25";
        public static final String BLEND_DIFFERENCE = "26";
        public static final String BLEND_DISSOLVE = "27";
        public static final String BLEND_EXCLUSION = "28";
        public static final String BLEND_SOURCE_OVER = "29";
        public static final String BLEND_HARD_LIGHT = "30";
        public static final String BLEND_LIGHTEN = "31";
        public static final String BLEND_ADD = "32";
        public static final String BLEND_DIVIDE = "33";
        public static final String BLEND_MULTIPLY = "34";
        public static final String BLEND_OVERLAY = "35";
        public static final String BLEND_SCREEN = "36";
        public static final String BLEND_ALPHA = "37";
        public static final String BLEND_COLOR = "38";
        public static final String BLEND_HUE = "39";
        public static final String BLEND_SATURATION = "40";
        public static final String BLEND_LUMINOSITY = "41";
        public static final String BLEND_LINEAR_BURN = "42";
        public static final String BLEND_SOFT_LIGHT = "43";
        public static final String BLEND_SUBTRACT = "44";
        public static final String BLEND_CHROMA_KEY = "45";
        public static final String BLEND_NORMAL = "46";
        public static final String LOOKUP_AMATORKA = "47";
        public static final String I_1977 = "48";//创新
        public static final String I_AMARO = "49";//流年
        public static final String I_BRANNAN = "50";//淡雅
        public static final String I_EARLYBIRD = "51";//怡尚
        public static final String I_HEFE = "52";//优格
        public static final String I_HUDSON = "53";//胶片
        public static final String I_INKWELL = "54";//黑白
        public static final String I_LOMO = "55";//个性
        public static final String I_LORDKELVIN = "56";//回忆
        public static final String I_NASHVILLE = "57";//复古
        public static final String I_RISE = "58";//森系
        public static final String I_SIERRA = "59";//清新
        public static final String I_SUTRO = "60";//摩登
        public static final String I_TOASTER = "61";//绚丽
        public static final String I_VALENCIA = "62";//优雅
        public static final String I_WALDEN = "63";//日系
        public static final String I_XPROII = "64";//新潮
        public static final String DEFAULT = "65";//原图

//        CONTRAST, GRAYSCALE, SHARPEN, SEPIA, SOBEL_EDGE_DETECTION, THREE_X_THREE_CONVOLUTION, FILTER_GROUP, EMBOSS, POSTERIZE, GAMMA, BRIGHTNESS, INVERT, HUE, PIXELATION,
//        SATURATION, EXPOSURE, HIGHLIGHT_SHADOW, MONOCHROME, OPACITY, RGB, WHITE_BALANCE, VIGNETTE, TONE_CURVE, BLEND_COLOR_BURN, BLEND_COLOR_DODGE, BLEND_DARKEN, BLEND_DIFFERENCE,
//        BLEND_DISSOLVE, BLEND_EXCLUSION, BLEND_SOURCE_OVER, BLEND_HARD_LIGHT, BLEND_LIGHTEN, BLEND_ADD, BLEND_DIVIDE, BLEND_MULTIPLY, BLEND_OVERLAY, BLEND_SCREEN, BLEND_ALPHA,
//        BLEND_COLOR, BLEND_HUE, BLEND_SATURATION, BLEND_LUMINOSITY, BLEND_LINEAR_BURN, BLEND_SOFT_LIGHT, BLEND_SUBTRACT, BLEND_CHROMA_KEY, BLEND_NORMAL, LOOKUP_AMATORKA,
//        I_1977, I_AMARO, I_BRANNAN, I_EARLYBIRD, I_HEFE, I_HUDSON, I_IMAGE,I_INKWELL, I_LOMO, I_LORDKELVIN, I_NASHVILLE, I_RISE, I_SIERRA, I_SUTRO,
//        I_TOASTER, I_VALENCIA, I_WALDEN, I_XPROII;
    }

//    public static class FilterList {
//        public List<String> names = new LinkedList<String>();
//        public List<FilterType> filters = new LinkedList<FilterType>();
//
//        public void addFilter(final String name, final FilterType filter) {
//            names.add(name);
//            filters.add(filter);
//        }
//    }

    public static class FilterAdjuster {
        private final Adjuster<? extends GPUImageFilter> adjuster;

        public FilterAdjuster(final GPUImageFilter filter) {
            if (filter instanceof GPUImageSharpenFilter) {
                adjuster = new SharpnessAdjuster().filter(filter);
            } else if (filter instanceof GPUImageSepiaFilter) {
                adjuster = new SepiaAdjuster().filter(filter);
            } else if (filter instanceof GPUImageContrastFilter) {
                adjuster = new ContrastAdjuster().filter(filter);
            } else if (filter instanceof GPUImageGammaFilter) {
                adjuster = new GammaAdjuster().filter(filter);
            } else if (filter instanceof GPUImageBrightnessFilter) {
                adjuster = new BrightnessAdjuster().filter(filter);
            } else if (filter instanceof GPUImageSobelEdgeDetection) {
                adjuster = new SobelAdjuster().filter(filter);
            } else if (filter instanceof GPUImage3x3TextureSamplingFilter) {
                adjuster = new GPU3x3TextureAdjuster().filter(filter);
            } else if (filter instanceof GPUImageEmbossFilter) {
                adjuster = new EmbossAdjuster().filter(filter);
            } else if (filter instanceof GPUImageHueFilter) {
                adjuster = new HueAdjuster().filter(filter);
            } else if (filter instanceof GPUImagePosterizeFilter) {
                adjuster = new PosterizeAdjuster().filter(filter);
            } else if (filter instanceof GPUImagePixelationFilter) {
                adjuster = new PixelationAdjuster().filter(filter);
            } else if (filter instanceof GPUImageSaturationFilter) {
                adjuster = new SaturationAdjuster().filter(filter);
            } else if (filter instanceof GPUImageExposureFilter) {
                adjuster = new ExposureAdjuster().filter(filter);
            } else if (filter instanceof GPUImageHighlightShadowFilter) {
                adjuster = new HighlightShadowAdjuster().filter(filter);
            } else if (filter instanceof GPUImageMonochromeFilter) {
                adjuster = new MonochromeAdjuster().filter(filter);
            } else if (filter instanceof GPUImageOpacityFilter) {
                adjuster = new OpacityAdjuster().filter(filter);
            } else if (filter instanceof GPUImageRGBFilter) {
                adjuster = new RGBAdjuster().filter(filter);
            } else if (filter instanceof GPUImageWhiteBalanceFilter) {
                adjuster = new WhiteBalanceAdjuster().filter(filter);
            } else if (filter instanceof GPUImageVignetteFilter) {
                adjuster = new VignetteAdjuster().filter(filter);
            } else if (filter instanceof GPUImageDissolveBlendFilter) {
                adjuster = new DissolveBlendAdjuster().filter(filter);
            } else {
                adjuster = null;
            }
        }

        public void adjust(final int percentage) {
            if (adjuster != null) {
                adjuster.adjust(percentage);
            }
        }

        private abstract class Adjuster<T extends GPUImageFilter> {
            private T filter;

            @SuppressWarnings("unchecked")
            public Adjuster<T> filter(final GPUImageFilter filter) {
                this.filter = (T) filter;
                return this;
            }

            public T getFilter() {
                return filter;
            }

            public abstract void adjust(int percentage);

            protected float range(final int percentage, final float start, final float end) {
                return (end - start) * percentage / 100.0f + start;
            }

            protected int range(final int percentage, final int start, final int end) {
                return (end - start) * percentage / 100 + start;
            }
        }

        private class SharpnessAdjuster extends Adjuster<GPUImageSharpenFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setSharpness(range(percentage, -4.0f, 4.0f));
            }
        }

        private class PixelationAdjuster extends Adjuster<GPUImagePixelationFilter> {
          @Override
          public void adjust(final int percentage) {
              getFilter().setPixel(range(percentage, 1.0f, 100.0f));
          }
        }

        private class HueAdjuster extends Adjuster<GPUImageHueFilter> {
          @Override
          public void adjust(final int percentage) {
            getFilter().setHue(range(percentage, 0.0f, 360.0f));
          }
        }

        private class ContrastAdjuster extends Adjuster<GPUImageContrastFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setContrast(range(percentage, 0.0f, 2.0f));
            }
        }

        private class GammaAdjuster extends Adjuster<GPUImageGammaFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setGamma(range(percentage, 0.0f, 3.0f));
            }
        }

        private class BrightnessAdjuster extends Adjuster<GPUImageBrightnessFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setBrightness(range(percentage, -1.0f, 1.0f));
            }
        }

        private class SepiaAdjuster extends Adjuster<GPUImageSepiaFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setIntensity(range(percentage, 0.0f, 2.0f));
            }
        }

        private class SobelAdjuster extends Adjuster<GPUImageSobelEdgeDetection> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setLineSize(range(percentage, 0.0f, 5.0f));
            }
        }

        private class EmbossAdjuster extends Adjuster<GPUImageEmbossFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setIntensity(range(percentage, 0.0f, 4.0f));
            }
        }

        private class PosterizeAdjuster extends Adjuster<GPUImagePosterizeFilter> {
            @Override
            public void adjust(final int percentage) {
                // In theorie to 256, but only first 50 are interesting
                getFilter().setColorLevels(range(percentage, 1, 50));
            }
        }

        private class GPU3x3TextureAdjuster extends Adjuster<GPUImage3x3TextureSamplingFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setLineSize(range(percentage, 0.0f, 5.0f));
            }
        }

        private class SaturationAdjuster extends Adjuster<GPUImageSaturationFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setSaturation(range(percentage, 0.0f, 2.0f));
            }
        }
        
        private class ExposureAdjuster extends Adjuster<GPUImageExposureFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setExposure(range(percentage, -10.0f, 10.0f));
            }
        }   
        
        private class HighlightShadowAdjuster extends Adjuster<GPUImageHighlightShadowFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setShadows(range(percentage, 0.0f, 1.0f));
                getFilter().setHighlights(range(percentage, 0.0f, 1.0f));
            }
        }
        
        private class MonochromeAdjuster extends Adjuster<GPUImageMonochromeFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setIntensity(range(percentage, 0.0f, 1.0f));
                //getFilter().setColor(new float[]{0.6f, 0.45f, 0.3f, 1.0f});
            }
        }
        
        private class OpacityAdjuster extends Adjuster<GPUImageOpacityFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setOpacity(range(percentage, 0.0f, 1.0f));
            }
        }   
        
        private class RGBAdjuster extends Adjuster<GPUImageRGBFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setRed(range(percentage, 0.0f, 1.0f));
                //getFilter().setGreen(range(percentage, 0.0f, 1.0f));
                //getFilter().setBlue(range(percentage, 0.0f, 1.0f));
            }
        }   
        
        private class WhiteBalanceAdjuster extends Adjuster<GPUImageWhiteBalanceFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setTemperature(range(percentage, 2000.0f, 8000.0f));
                //getFilter().setTint(range(percentage, -100.0f, 100.0f));
            }
        }

        private class VignetteAdjuster extends Adjuster<GPUImageVignetteFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setVignetteStart(range(percentage, 0.0f, 1.0f));
            }
        }

        private class DissolveBlendAdjuster extends Adjuster<GPUImageDissolveBlendFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setMix(range(percentage, 0.0f, 1.0f));
            }
        }
    }
}
