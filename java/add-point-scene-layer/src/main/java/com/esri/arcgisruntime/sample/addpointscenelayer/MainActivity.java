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

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISSceneLayer;
import com.esri.arcgisruntime.mapping.ArcGISScene;
import com.esri.arcgisruntime.mapping.ArcGISTiledElevationSource;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Surface;
import com.esri.arcgisruntime.mapping.view.Camera;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.SceneView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SceneSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSceneSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.esri.arcgisruntime.symbology.TextSymbol;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SceneView mSceneView;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSceneView = findViewById(R.id.sceneView);

        // create a scene with a basemap and add it to the scene view
        ArcGISScene scene = new ArcGISScene(Basemap.Type.IMAGERY);
        mSceneView.setScene(scene);


        // mSceneView.setViewpointAsync(new Viewpoint(latitude, longitude, 10000), 200);
        mSceneView.setViewpointCameraAsync(new Camera(30.457091,
                114.398683, 1000, 0, 0, 0));

        // create mark
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID,
                Color.BLUE, 5);
        // create render
        SimpleRenderer mSimpleRenderer = new SimpleRenderer(lineSymbol);
        // create a graphics overlay
        GraphicsOverlay mTrackerGraphicsOverlay = new GraphicsOverlay();
        mTrackerGraphicsOverlay.setRenderer(mSimpleRenderer);
        Graphic mFlightSimpleGraphic = new Graphic();
        mSceneView.getGraphicsOverlays().add(mTrackerGraphicsOverlay);


        // set the base surface with world elevation
        // Surface surface = new Surface();
        // surface.getElevationSources().add(new ArcGISTiledElevationSource(getString(R.string
        // .elevation_image_service)));
        // scene.setBaseSurface(surface);

        // add a point scene layer with points at world airport locations
        // ArcGISSceneLayer pointSceneLayer = new ArcGISSceneLayer(getString(R.string
        // .world_airports_scene_layer));
        // scene.getOperationalLayers().add(pointSceneLayer);
        SimpleMarkerSceneSymbol buoyMarker5 = new SimpleMarkerSceneSymbol(
                SimpleMarkerSceneSymbol.Style.DIAMOND, Color.BLUE, 10,
                10, 10, SceneSymbol.AnchorPosition.CENTER);
        Point wgs = new Point(114.398683, 30.457091, 1000, SpatialReferences.getWgs84());
        Point point = (Point) GeometryEngine.project(wgs, SpatialReferences.getWebMercator());
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


        PictureMarkerSymbol mPinSourceSymbol = null;
        // define pin drawable
        BitmapDrawable pinDrawable = (BitmapDrawable) ContextCompat.getDrawable(this, R
                .drawable.flight_nav_ic);
        try {
            mPinSourceSymbol = PictureMarkerSymbol.createAsync(pinDrawable).get();
            mPinSourceSymbol.setHeight(15);
            mPinSourceSymbol.setWidth(15);
        } catch (InterruptedException | ExecutionException e) {
            String error = "Error creating PictureMarkerSymbol: " + e.getMessage();
            Log.e(TAG, error);
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }

        if (mPinSourceSymbol != null) {
            Graphic resultLocGraphic = new Graphic(point, mPinSourceSymbol);
            mTrackerGraphicsOverlay.getGraphics().add(resultLocGraphic);
        }

        TextSymbol textSymbol =new TextSymbol();
        textSymbol.setText("12");
        textSymbol.setColor(Color.WHITE);
        textSymbol.setHorizontalAlignment(TextSymbol.HorizontalAlignment.LEFT);
        textSymbol.setVerticalAlignment(TextSymbol.VerticalAlignment.TOP);
        textSymbol.setSize(10);
        // textSymbol.setOffsetY(20);
        wgs = new Point(114.398683, 30.457800, 1000, SpatialReferences.getWgs84());
        point = (Point) GeometryEngine.project(wgs, SpatialReferences.getWebMercator());
        Graphic textGraphic = new Graphic(point, textSymbol);
        mTrackerGraphicsOverlay.getGraphics().add(textGraphic);

        // TextSymbol relativeText = new TextSymbol(15, "RELATIVE", Color.MAGENTA,
        //         TextSymbol.HorizontalAlignment.LEFT,
        //         TextSymbol.VerticalAlignment.TOP);
        // relativeText.setOffsetY(20);
        // mTrackerGraphicsOverlay.getGraphics().add(new Graphic(point, relativeText));

        SimpleMarkerSymbol buoyMarker6 = new SimpleMarkerSymbol(
                SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 10);
        wgs = new Point(114.398683, 30.457800, 1000, SpatialReferences.getWgs84());
        point = (Point) GeometryEngine.project(wgs, SpatialReferences.getWebMercator());
        Graphic bgGraphic = new Graphic(point, buoyMarker6);
        mTrackerGraphicsOverlay.getGraphics().add(bgGraphic);

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
}
