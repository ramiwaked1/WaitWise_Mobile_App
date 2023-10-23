package com.app.groundfloor24.clinicsmap;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.app.groundfloor24.clinicsmap.model.MyItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomClusterRenderer extends DefaultClusterRenderer<MyItem> {


    // it for changing cluster color and its size

    Context context;
    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected int getColor(int clusterSize) {
        return ContextCompat.getColor(context, R.color.teal_200);
    }

    @Override
    protected boolean shouldRenderAsCluster(@NonNull Cluster<MyItem> cluster) {
        return cluster.getSize() > 8;
    }

//    @Override
//    protected void onBeforeClusterRendered(@NonNull Cluster<MyItem> cluster, @NonNull MarkerOptions markerOptions) {
//
//
//
//        Bitmap bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_station);
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bm)));
//        //super.onBeforeClusterRendered(cluster, markerOptions);
//
//    }





}