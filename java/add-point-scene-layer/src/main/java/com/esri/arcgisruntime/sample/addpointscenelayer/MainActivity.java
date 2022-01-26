/*
 *  Copyright 2019 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.esri.arcgisruntime.sample.addpointscenelayer;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PolygonBuilder;
import com.esri.arcgisruntime.geometry.PolylineBuilder;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISScene;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Camera;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerSceneProperties;
import com.esri.arcgisruntime.mapping.view.SceneView;
import com.esri.arcgisruntime.sample.addpointscenelayer.databinding.ActivityMainBinding;
import com.esri.arcgisruntime.symbology.MarkerSceneSymbol;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SceneSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSceneSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.esri.arcgisruntime.util.ListenableList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SceneView mSceneView;
    private String mUrl;

    private GraphicsOverlay mGraphicsLineOverlay;
    private Graphic mGraphicLine;
    private SimpleRenderer mLineRenderer;

    private GraphicsOverlay mPointGraphicOverlay;
    private Graphic mGraphicPoint;
    private SimpleRenderer mPointRenderer;

    private GraphicsOverlay mPolygonOverlay;
    private Graphic mGraphicPolygon;
    private SimpleRenderer mPolygonRenderer;
    private SimpleFillSymbol mPolygonSymbol;


    private GraphicsOverlay mGraphicsIconAirplaneOverlay;
    private GraphicsOverlay mGraphicsIconSelectOverlay;
    private GraphicsOverlay mGraphicsIconHomeOverlay;
    private GraphicsOverlay mGraphicsIconsOtherOverlay;
    private SimpleRenderer mIconsAirplaneRenderer;
    private SimpleRenderer mIconsSelectRenderer;
    private SimpleRenderer mIconsHomeRenderer;
    private SimpleRenderer mIconsOtherRenderer;
    private Graphic mIconHomeGraphic;
    private Graphic mIconAirplaneGraphic;
    private Graphic mSelectedGraphic;
    private Graphic mOtherGraphic;

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mSceneView = findViewById(R.id.sceneView);

        // create a scene with a basemap and add it to the scene view
        ArcGISScene scene = new ArcGISScene(Basemap.Type.IMAGERY);
        mSceneView.setScene(scene);
        mSceneView.setViewpointCameraAsync(new Camera(30.457091,
                114.398683, 1000, 0, 0, 0));

        // create mark
        // SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID,
        //         Color.BLUE, 5);
        // // create render
        // SimpleRenderer mSimpleRenderer = new SimpleRenderer(lineSymbol);
        // // create a graphics overlay
        // GraphicsOverlay mTrackerGraphicsOverlay = new GraphicsOverlay();
        // mTrackerGraphicsOverlay.setRenderer(mSimpleRenderer);
        // mSceneView.getGraphicsOverlays().add(mTrackerGraphicsOverlay);

        initTest();
        initFlightPointSymbol();
        mBinding.btnTestLine.setOnClickListener(v -> {
            doTest();
            // addGraphicsOverlay();
        });
        mBinding.btnTestIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doIconTest();
            }
        });

        // // set the base surface with world elevation
        // Surface surface = new Surface();
        // surface.addLoadStatusChangedListener(new LoadStatusChangedListener() {
        //     @Override
        //     public void loadStatusChanged(LoadStatusChangedEvent loadStatusChangedEvent) {
        //         if (loadStatusChangedEvent.getNewLoadStatus() == LoadStatus.LOADED) {
        //             initTest();
        //             initFlightPointSymbol();
        //             mBinding.btnTestLine.setOnClickListener(v -> {
        //                 doTest();
        //                 // addGraphicsOverlay();
        //             });
        //             mBinding.btnTestIcon.setOnClickListener(new View.OnClickListener() {
        //                 @Override
        //                 public void onClick(View v) {
        //                     doIconTest();
        //                 }
        //             });
        //         }
        //     }
        // });
        // surface.getElevationSources().add(new ArcGISTiledElevationSource(getString(R.string
        //         .elevation_image_service)));
        // scene.setBaseSurface(surface);

        // add a point scene layer with points at world airport locations
        // ArcGISSceneLayer pointSceneLayer = new ArcGISSceneLayer(getString(R.string
        //         .world_airports_scene_layer));
        // scene.getOperationalLayers().add(pointSceneLayer);

        // 凸显高度
        // graphics3DOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties
        // .SurfacePlacement.DRAPED_FLAT);
        // mSceneView.getGraphicsOverlays().add(graphics3DOverlay);
        // mSceneView.setViewpointAsync(new Viewpoint(latitude, longitude, 10000), 200);


        // SimpleMarkerSceneSymbol buoyMarker5 = new SimpleMarkerSceneSymbol(
        //         SimpleMarkerSceneSymbol.Style.DIAMOND, Color.BLUE, 10,
        //         10, 10, SceneSymbol.AnchorPosition.CENTER);
        // Point wgs = new Point(114.398683, 30.457091, 1000, SpatialReferences.getWgs84());
        // Point point = (Point) GeometryEngine.project(wgs, SpatialReferences.getWebMercator());
        // mFlightSimpleGraphic.setSymbol(buoyMarker5);
        // mFlightSimpleGraphic.setGeometry(point);
        // mTrackerGraphicsOverlay.getGraphics().add(mFlightSimpleGraphic);

        // url获取失败
        // Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
        //         /*+ getResources().getResourcePackageName(R.drawable.flight_nav_ic) + "/"
        //         + getResources().getResourceTypeName(R.drawable.flight_nav_ic) + "/"
        //         + getResources().getResourceEntryName(R.drawable.flight_nav_ic)+ "/"*/
        //         + getResources().getResourceName(R.drawable.flight_nav_ic)
        // );
        // mUrl = uri.toString();
        // PictureMarkerSymbol ps = new PictureMarkerSymbol(mUrl);
        // if (ps != null) {
        //     ps.setHeight(15);
        //     ps.setWidth(15);
        //     Graphic resultLocGraphic = new Graphic(point, ps);
        //     mTrackerGraphicsOverlay.getGraphics().add(resultLocGraphic);
        // }


        // PictureMarkerSymbol mPinSourceSymbol = null;
        // // define pin drawable
        // BitmapDrawable pinDrawable = (BitmapDrawable) ContextCompat.getDrawable(this, R
        //         .drawable.flight_nav_ic);
        // try {
        //     mPinSourceSymbol = PictureMarkerSymbol.createAsync(pinDrawable).get();
        //     mPinSourceSymbol.setHeight(15);
        //     mPinSourceSymbol.setWidth(15);
        // } catch (InterruptedException | ExecutionException e) {
        //     String error = "Error creating PictureMarkerSymbol: " + e.getMessage();
        //     Log.e(TAG, error);
        //     Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        // }
        //
        // if (mPinSourceSymbol != null) {
        //     Graphic resultLocGraphic = new Graphic(point, mPinSourceSymbol);
        //     mTrackerGraphicsOverlay.getGraphics().add(resultLocGraphic);
        // }
        //
        // TextSymbol textSymbol =new TextSymbol();
        // textSymbol.setText("12");
        // textSymbol.setColor(Color.WHITE);
        // textSymbol.setHorizontalAlignment(TextSymbol.HorizontalAlignment.LEFT);
        // textSymbol.setVerticalAlignment(TextSymbol.VerticalAlignment.TOP);
        // textSymbol.setSize(10);
        // // textSymbol.setOffsetY(20);
        // wgs = new Point(114.398683, 30.457800, 1000, SpatialReferences.getWgs84());
        // point = (Point) GeometryEngine.project(wgs, SpatialReferences.getWebMercator());
        // Graphic textGraphic = new Graphic(point, textSymbol);
        // mTrackerGraphicsOverlay.getGraphics().add(textGraphic);
        //
        // // TextSymbol relativeText = new TextSymbol(15, "RELATIVE", Color.MAGENTA,
        // //         TextSymbol.HorizontalAlignment.LEFT,
        // //         TextSymbol.VerticalAlignment.TOP);
        // // relativeText.setOffsetY(20);
        // // mTrackerGraphicsOverlay.getGraphics().add(new Graphic(point, relativeText));
        //
        // SimpleMarkerSymbol buoyMarker6 = new SimpleMarkerSymbol(
        //         SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 10);
        // wgs = new Point(114.398683, 30.457800, 1000, SpatialReferences.getWgs84());
        // point = (Point) GeometryEngine.project(wgs, SpatialReferences.getWebMercator());
        // Graphic bgGraphic = new Graphic(point, buoyMarker6);
        // mTrackerGraphicsOverlay.getGraphics().add(bgGraphic);

        // SimpleMarkerSceneSymbol buoyMarker6 = new SimpleMarkerSceneSymbol(
        //         SimpleMarkerSceneSymbol.Style.CYLINDER, Color.BLUE, 10,
        //         10, 10, SceneSymbol.AnchorPosition.CENTER);
        // wgs = new Point(114.398683, 30.457200, 1000, SpatialReferences.getWgs84());
        // point = (Point) GeometryEngine.project(wgs, SpatialReferences.getWebMercator());
        // Graphic bgGraphic = new Graphic(point, buoyMarker6);
        // mTrackerGraphicsOverlay.getGraphics().add(bgGraphic);

    }

    @Override
    protected void onPause() {
        mSceneView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSceneView.resume();
    }

    @Override
    protected void onDestroy() {
        mSceneView.dispose();
        super.onDestroy();
    }

    private MarkerSceneSymbol mScenePointSymbol;
    private SimpleMarkerSymbol mSimplePointSymbol1;
    private SimpleMarkerSymbol mSimplePointSymbol2;
    private SimpleMarkerSymbol mSimplePointSymbol3;
    private SimpleMarkerSymbol mSimplePointSymbol4;

    private void initTest() {
        // create graphic for point
        // SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND,
        //         Color.RED, 10);
        mScenePointSymbol = new SimpleMarkerSceneSymbol(SimpleMarkerSceneSymbol.Style
                .CUBE, Color.RED, 600, 100, 10, SceneSymbol.AnchorPosition.ORIGIN);

        mSimplePointSymbol1 = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.WHITE,
                10);
        mSimplePointSymbol2 = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED,
                10);
        mSimplePointSymbol3 = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.GREEN,
                10);
        mSimplePointSymbol4 = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE,
                10);

        // 114.479057345, 30.457098828
        Point wgsP = new Point(114.397000, 30.456091, 50, SpatialReferences.getWgs84());
        Point mp = (Point) GeometryEngine.project(wgsP, SpatialReferences.getWebMercator());
        mGraphicPoint = new Graphic(mp, mScenePointSymbol);
        // create a point graphic overlay for the point
        mPointGraphicOverlay = new GraphicsOverlay();
        mPointGraphicOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties
                .SurfacePlacement.RELATIVE);
        mSceneView.getGraphicsOverlays().add(mPointGraphicOverlay);
        // create simple renderer
        mPointRenderer = new SimpleRenderer(mScenePointSymbol);
        mPointGraphicOverlay.setRenderer(mPointRenderer);
        mPointGraphicOverlay.getGraphics().add(mGraphicPoint);

        // create graphic for a line
        mGraphicLine = new Graphic();
        // create a line graphic overlay
        mGraphicsLineOverlay = new GraphicsOverlay();
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID,
                Color.GREEN, 2,
                SimpleLineSymbol.MarkerStyle.NONE, SimpleLineSymbol.MarkerPlacement.BEGIN_AND_END);
        mGraphicsLineOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties
                .SurfacePlacement.RELATIVE);
        mSceneView.getGraphicsOverlays().add(mGraphicsLineOverlay);
        // create render for line
        mLineRenderer = new SimpleRenderer(lineSymbol);
        mGraphicsLineOverlay.setRenderer(mLineRenderer);
        mGraphicsLineOverlay.getGraphics().add(mGraphicLine);

        // create graphic for a line
        // mGraphicPolygon = new Graphic();
        // // create a line graphic overlay
        // mPolygonOverlay = new GraphicsOverlay();
        // SimpleFillSymbol polygonSymbol =
        //         new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID,
        //                 0x80FFFF00, null);
        // mPolygonOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties
        //         .SurfacePlacement.RELATIVE);
        // mSceneView.getGraphicsOverlays().add(mPolygonOverlay);
        // // create render for line
        // mPolygonRenderer = new SimpleRenderer(polygonSymbol);
        // mPolygonOverlay.setRenderer(mPolygonRenderer);
        // mPolygonOverlay.getGraphics().add(mGraphicPolygon);
    }

    private Graphic mGMp1;
    private Graphic mGMp2;
    private Graphic mGMp3;
    private Graphic mGMp4;

    private Graphic mGraphicPolygonLine;

    private void doTest() {
        //30.457091, 114.398683
        PolylineBuilder lineBuilder = new PolylineBuilder(SpatialReferences.getWebMercator());
        Point wgs84 = new Point(114.398683, 30.457091,
                300, SpatialReferences.getWgs84());
        Point point = (Point) GeometryEngine.project(wgs84, SpatialReferences.getWebMercator());
        lineBuilder.addPoint(point);

        wgs84 = new Point(114.396000, 30.457091,
                300, SpatialReferences.getWgs84());
        point = (Point) GeometryEngine.project(wgs84, SpatialReferences.getWebMercator());
        lineBuilder.addPoint(point);

        // wgs84 = new Point(114.397293, 30.456742, 130, SpatialReferences.getWgs84());
        // point = (Point) GeometryEngine.project(wgs84, SpatialReferences.getWebMercator());
        // lineBuilder.addPoint(point);
        mGraphicLine.setGeometry(lineBuilder.toGeometry());
        mGraphicsLineOverlay.setRenderer(mLineRenderer);


        if (mGraphicPolygon == null) {
            // create a line graphic overlay
            mPolygonOverlay = new GraphicsOverlay();
            mPolygonOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties
                    .SurfacePlacement.RELATIVE);
            mSceneView.getGraphicsOverlays().add(mPolygonOverlay);
        }


        PolylineBuilder polyline = new PolylineBuilder(SpatialReferences.getWebMercator());
        PolygonBuilder polygonBuilder = new PolygonBuilder(SpatialReferences.getWgs84());
        Point polyP1Wgs = new Point(114.398683, 30.458091, 100,
                SpatialReferences.getWgs84());
        Point polyMp1 = (Point) GeometryEngine.project(polyP1Wgs,
                SpatialReferences.getWebMercator());
        if (mGMp1 == null) {
            mGMp1 = new Graphic(polyMp1, mSimplePointSymbol1);
            mPolygonOverlay.getGraphics().add(mGMp1);
        } else {
            mGMp1.setGeometry(polyMp1);
        }
        polyline.addPoint(polyMp1);
        polygonBuilder.addPoint(polyP1Wgs);

        Point polyP2Wgs = new Point(114.396000, 30.457091, 100,
                SpatialReferences.getWgs84());
        Point polyMp2 = (Point) GeometryEngine.project(polyP2Wgs,
                SpatialReferences.getWebMercator());
        if (mGMp2 == null) {
            mGMp2 = new Graphic(polyMp2, mSimplePointSymbol2);
            mPolygonOverlay.getGraphics().add(mGMp2);
        } else {
            mGMp2.setGeometry(polyMp2);
        }
        polyline.addPoint(polyMp2);
        polygonBuilder.addPoint(polyP2Wgs);

        Point polyP3Wgs = new Point(114.396000, 30.457091, 0,
                SpatialReferences.getWgs84());
        Point polyMp3 = (Point) GeometryEngine.project(polyP3Wgs,
                SpatialReferences.getWebMercator());
        if (mGMp3 == null) {
            mGMp3 = new Graphic(polyMp3, mSimplePointSymbol3);
            mPolygonOverlay.getGraphics().add(mGMp3);
        } else {
            mGMp3.setGeometry(polyMp3);
        }
        polyline.addPoint(polyMp3);
        polygonBuilder.addPoint(polyP3Wgs);
        //
        Point polyP4Wgs = new Point(114.398683, 30.458091, 0,
                SpatialReferences.getWgs84());
        Point polyMp4 = (Point) GeometryEngine.project(polyP4Wgs,
                SpatialReferences.getWebMercator());
        if (mGMp4 == null) {
            mGMp4 = new Graphic(polyMp4, mSimplePointSymbol4);
            mPolygonOverlay.getGraphics().add(mGMp4);
        } else {
            mGMp4.setGeometry(polyMp4);
        }
        polyline.addPoint(polyMp4);
        polygonBuilder.addPoint(polyP4Wgs);
        polyline.addPoint(polyMp1);
        // polygonBuilder.addPoint(polyMp1);

        if (mGraphicPolygon == null) {
            mPolygonSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID,
                    Color.YELLOW, null);
            mGraphicPolygon = new Graphic(polygonBuilder.toGeometry(), mPolygonSymbol);
            mPolygonOverlay.getGraphics().add(mGraphicPolygon);
        } else {
            mGraphicPolygon.setGeometry(polygonBuilder.toGeometry());
        }


        if (mGraphicPolygonLine == null) {
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID,
                    Color.GREEN, 2,
                    SimpleLineSymbol.MarkerStyle.NONE,
                    SimpleLineSymbol.MarkerPlacement.BEGIN_AND_END);

            mGraphicPolygonLine = new Graphic(polyline.toGeometry(), lineSymbol);
            mPolygonOverlay.getGraphics().add(mGraphicPolygonLine);
        } else {
            mGraphicPolygonLine.setGeometry(polyline.toGeometry());
        }


        // create render for line
        // mPolygonOverlay.setRenderer(mPolygonRenderer);
        mGraphicPolygon.setGeometry(polygonBuilder.toGeometry());
        if (!mPolygonOverlay.getGraphics().contains(mGraphicPolygon)) {
            mPolygonOverlay.getGraphics().add(mGraphicPolygon);
        }

    }

    private void addGraphicsOverlay() {
        mSceneView.setViewpoint(new Viewpoint(3.184710, -4.734690, 100000000));

        // create the polygon
        PolygonBuilder polygonGeometry = new PolygonBuilder(SpatialReferences.getWebMercator());
        polygonGeometry.addPoint(-20e5, 20e5, 100);
        polygonGeometry.addPoint(20e5, 20.e5, 100);
        polygonGeometry.addPoint(20e5, -20e5, 0);
        polygonGeometry.addPoint(-20e5, -20e5);

        // create solid line symbol
        SimpleFillSymbol polygonSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID,
                Color.YELLOW, null);
        // create graphic from polygon geometry and symbol
        Graphic graphic = new Graphic(polygonGeometry.toGeometry(), polygonSymbol);

        // create graphics overlay
        GraphicsOverlay grOverlay = new GraphicsOverlay();
        grOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties
                .SurfacePlacement.ABSOLUTE);
        // create list of graphics
        ListenableList<Graphic> graphics = grOverlay.getGraphics();
        // add graphic to graphics overlay
        graphics.add(graphic);
        // add graphics overlay to the MapView
        mSceneView.getGraphicsOverlays().add(grOverlay);
    }


    private BitmapDrawable mAutoCreatedDrawable;
    private BitmapDrawable mOtherStatusDrawable;
    private BitmapDrawable mFlyStartDrawable;
    private BitmapDrawable mSelectedDrawable;
    private static final int SYMBOL_SIZE_UNIT_DP = 24;
    private PictureMarkerSymbol mAirplaneSymbol;
    private PictureMarkerSymbol mOtherSymbol;
    private PictureMarkerSymbol mHomeSymbol;
    private PictureMarkerSymbol mSelectedSymbol;

    private void initFlightPointSymbol() {
        if (mAirplaneSymbol == null) {
            mAutoCreatedDrawable = (BitmapDrawable) ContextCompat.getDrawable(
                    mSceneView.getContext(), R.drawable.icon_airplane);
            try {
                assert mAutoCreatedDrawable != null;
                mAirplaneSymbol = PictureMarkerSymbol.createAsync(mAutoCreatedDrawable).get();
            } catch (Exception e) {
                // Timber.e(e);
                return;
            }
            mAirplaneSymbol.setHeight(SYMBOL_SIZE_UNIT_DP);
            mAirplaneSymbol.setWidth(SYMBOL_SIZE_UNIT_DP);
        }

        if (mSelectedSymbol == null) {
            mSelectedDrawable = (BitmapDrawable) ContextCompat.getDrawable(
                    mSceneView.getContext(), R.drawable.survey_area_final_touched_point_icon);
            try {
                assert mSelectedDrawable != null;
                mSelectedSymbol = PictureMarkerSymbol.createAsync(mSelectedDrawable).get();
            } catch (Exception e) {
                // Timber.e(e);
                return;
            }
            mSelectedSymbol.setHeight(SYMBOL_SIZE_UNIT_DP);
            mSelectedSymbol.setWidth(SYMBOL_SIZE_UNIT_DP);
        }

        if (mOtherSymbol == null) {
            mOtherStatusDrawable = (BitmapDrawable) ContextCompat.getDrawable(
                    mSceneView.getContext(), R.drawable.survey_area_other_status_icon);
            try {
                assert mOtherStatusDrawable != null;
                mOtherSymbol = PictureMarkerSymbol.createAsync(mOtherStatusDrawable).get();
            } catch (Exception e) {
                // Timber.e(e);
                return;
            }
            mOtherSymbol.setHeight(SYMBOL_SIZE_UNIT_DP);
            mOtherSymbol.setWidth(SYMBOL_SIZE_UNIT_DP);
        }

        if (mFlyStartDrawable == null) {
            mFlyStartDrawable = (BitmapDrawable) ContextCompat.getDrawable(
                    mSceneView.getContext(), R.drawable.icon_home_point_location);
            try {
                assert mFlyStartDrawable != null;
                mHomeSymbol = PictureMarkerSymbol.createAsync(mFlyStartDrawable).get();
            } catch (Exception e) {
                // Timber.e(e);
                return;
            }
            mHomeSymbol.setHeight(SYMBOL_SIZE_UNIT_DP);
            mHomeSymbol.setWidth(SYMBOL_SIZE_UNIT_DP);
        }
    }

    private double mStepX = 0.000002;
    private double mStepY = 0.000002;
    private boolean mStarted = false;

    private void doIconTest() {
        if (mGraphicsIconsOtherOverlay == null) {
            mGraphicsIconAirplaneOverlay = new GraphicsOverlay();
            mGraphicsIconSelectOverlay = new GraphicsOverlay();
            mGraphicsIconHomeOverlay = new GraphicsOverlay();
            mGraphicsIconsOtherOverlay = new GraphicsOverlay();
            mGraphicsIconAirplaneOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.RELATIVE_TO_SCENE);
            mGraphicsIconSelectOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.RELATIVE_TO_SCENE);
            mGraphicsIconHomeOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.RELATIVE_TO_SCENE);
            mGraphicsIconsOtherOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.RELATIVE_TO_SCENE);

            SimpleLineSymbol iconSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID,
                    Color.BLUE, 2);
            SimpleLineSymbol iconSymbolW = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID,
                    Color.WHITE, 2);
            SimpleLineSymbol iconSymbolY = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID,
                    Color.YELLOW, 2);
            SimpleLineSymbol iconSymbolG = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID,
                    Color.GREEN, 2);
            mIconsAirplaneRenderer = new SimpleRenderer(iconSymbol);
            mIconsSelectRenderer = new SimpleRenderer(iconSymbol);
            mIconsHomeRenderer = new SimpleRenderer(iconSymbol);
            mIconsOtherRenderer = new SimpleRenderer(iconSymbol);
            mGraphicsIconAirplaneOverlay.setRenderer(mIconsAirplaneRenderer);
            mGraphicsIconSelectOverlay.setRenderer(mIconsSelectRenderer);
            mGraphicsIconHomeOverlay.setRenderer(mIconsHomeRenderer);
            mGraphicsIconsOtherOverlay.setRenderer(mIconsOtherRenderer);
            mSceneView.getGraphicsOverlays().add(mGraphicsIconAirplaneOverlay);
            mSceneView.getGraphicsOverlays().add(mGraphicsIconSelectOverlay);
            mSceneView.getGraphicsOverlays().add(mGraphicsIconHomeOverlay);
            mSceneView.getGraphicsOverlays().add(mGraphicsIconsOtherOverlay);


            mIconAirplaneGraphic = new Graphic(new Point(114.398683, 30.457091, 100,
                    SpatialReferences.getWgs84()), mAirplaneSymbol);
            mGraphicsIconAirplaneOverlay.getGraphics().add(mIconAirplaneGraphic);
            mSelectedGraphic = new Graphic(new Point(114.398683, 30.457091, 100,
                    SpatialReferences.getWgs84()), mSelectedSymbol);
            mGraphicsIconSelectOverlay.getGraphics().add(mSelectedGraphic);

            mIconHomeGraphic = new Graphic(new Point(114.398683, 30.457093, 100,
                    SpatialReferences.getWgs84()), mHomeSymbol);
            mGraphicsIconHomeOverlay.getGraphics().add(mIconHomeGraphic);
            mOtherGraphic = new Graphic(new Point(114.398683, 30.457093, 100,
                    SpatialReferences.getWgs84()), mOtherSymbol);
            mGraphicsIconsOtherOverlay.getGraphics().add(mOtherGraphic);

        } else {
            if (!mStarted) {
                mStepX = 114.398683;
                mStepY = 30.457093;
                mStarted = true;
            } else {
                mStepX += 0.000002f;
                mStepY += 0.000002f;
            }

            double x = mStepX + 0.000001f;
            double y = mStepY + 0.000001f;
            mIconAirplaneGraphic.setGeometry(new Point(x, y,504,
                    SpatialReferences.getWgs84()));
            mSelectedGraphic.setGeometry(new Point(x + 0.000002, y + 0.000002,303,
                    SpatialReferences.getWgs84()));

            x = mStepX + 0.000005f;
            y = mStepY + 0.000005f;
            mIconHomeGraphic.setGeometry(new Point(x + 0.000002, y + 0.000002,202,
                    SpatialReferences.getWgs84()));
            mOtherGraphic.setGeometry(new Point(x + 0.000002, y + 0.000002,101,
                    SpatialReferences.getWgs84()));
        }


    }

}
