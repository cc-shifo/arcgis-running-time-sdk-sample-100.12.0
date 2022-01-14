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
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PolylineBuilder;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISSceneLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.loadable.LoadStatusChangedEvent;
import com.esri.arcgisruntime.loadable.LoadStatusChangedListener;
import com.esri.arcgisruntime.mapping.ArcGISScene;
import com.esri.arcgisruntime.mapping.ArcGISTiledElevationSource;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Surface;
import com.esri.arcgisruntime.mapping.view.Camera;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerSceneProperties;
import com.esri.arcgisruntime.mapping.view.SceneView;
import com.esri.arcgisruntime.sample.addpointscenelayer.databinding.ActivityMainBinding;
import com.esri.arcgisruntime.symbology.MarkerSceneSymbol;
import com.esri.arcgisruntime.symbology.SceneSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSceneSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;

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


        // set the base surface with world elevation
        Surface surface = new Surface();
        surface.addLoadStatusChangedListener(new LoadStatusChangedListener() {
            @Override
            public void loadStatusChanged(LoadStatusChangedEvent loadStatusChangedEvent) {
                if (loadStatusChangedEvent.getNewLoadStatus() == LoadStatus.LOADED) {
                    initTest();
                    mBinding.btnTestLine.setOnClickListener(v -> {
                        doTest();
                    });
                }
            }
        });
        surface.getElevationSources().add(new ArcGISTiledElevationSource(getString(R.string
                .elevation_image_service)));
        scene.setBaseSurface(surface);

        // add a point scene layer with points at world airport locations
        ArcGISSceneLayer pointSceneLayer = new ArcGISSceneLayer(getString(R.string
                .world_airports_scene_layer));
        scene.getOperationalLayers().add(pointSceneLayer);

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

    private void initTest() {
        // create graphic for point
        // SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND,
        //         Color.RED, 10);
        MarkerSceneSymbol pointSymbol = new SimpleMarkerSceneSymbol(SimpleMarkerSceneSymbol.Style
        .CUBE, Color.RED, 600, 200, 10, SceneSymbol.AnchorPosition.ORIGIN);

        Point wgsP = new Point(114.479057345, 30.457098828, 100, SpatialReferences.getWgs84());
        Point mp = (Point) GeometryEngine.project(wgsP, SpatialReferences.getWebMercator());
        mGraphicPoint = new Graphic(mp, pointSymbol);
        // create a point graphic overlay for the point
        mPointGraphicOverlay = new GraphicsOverlay();
        mPointGraphicOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties
                .SurfacePlacement.RELATIVE);
        mSceneView.getGraphicsOverlays().add(mPointGraphicOverlay);
        // create simple renderer
        mPointRenderer = new SimpleRenderer(pointSymbol);
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
    }

    private void doTest() {
        PolylineBuilder lineBuilder = new PolylineBuilder(SpatialReferences.getWebMercator());
        Point wgs84 = new Point(114.479057345, 30.457098828,
                120, SpatialReferences.getWgs84());
        /* Viewpoint viewpoint = new Viewpoint(wgs84, 10000); */
        Point point = (Point) GeometryEngine.project(wgs84, SpatialReferences.getWebMercator());
        lineBuilder.addPoint(point);

        wgs84 = new Point(114.479058426, 30.457099828,
                120, SpatialReferences.getWgs84());
        point = (Point) GeometryEngine.project(wgs84, SpatialReferences.getWebMercator());
        lineBuilder.addPoint(point);

        wgs84 = new Point(114.397293, 30.456742, 130, SpatialReferences.getWgs84());
        point = (Point) GeometryEngine.project(wgs84, SpatialReferences.getWebMercator());
        lineBuilder.addPoint(point);
        mGraphicLine.setGeometry(lineBuilder.toGeometry());
        mGraphicsLineOverlay.setRenderer(mLineRenderer);
    }
}
