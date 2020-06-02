package com.example.withstudy.ui.studyroom;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.withstudy.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MapActivity extends AppCompatActivity implements MapView.POIItemEventListener, MapView.MapViewEventListener, MapView.CurrentLocationEventListener {
    private MapView mapView;
    private MapPOIItem studyLocationMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        initialize();
    }

    // 맵 데이터 초기화
    private void initialize() {
        ViewGroup mapViewContainer;

        mapView = new MapView(this);

        mapViewContainer = (ViewGroup)findViewById(R.id.map_mapView);

        // 트래킹 모드(나침반 효과 적용 X)
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        mapViewContainer.addView(mapView);

        // 맵 관련 Listener 등록
        mapView.setCurrentLocationEventListener(this);
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
    }

    // 반경 안에 있는지 검사
    private void checkRange() {
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        // 250m 반경
        mapView.setCurrentLocationRadius(250);
        System.out.println("x좌표 = " + mapPoint.getMapPointGeoCoord().latitude + ", y좌표 = " + mapPoint.getMapPointGeoCoord().longitude);
        mapView.setCurrentLocationRadiusFillColor(Color.argb(50, 0, 0, 255));
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        if(studyLocationMarker == null) {
            studyLocationMarker = new MapPOIItem();

            studyLocationMarker.setItemName("생성 위치");
            studyLocationMarker.setMapPoint(mapPoint);
            studyLocationMarker.setMarkerType(MapPOIItem.MarkerType.RedPin);

            mapView.addPOIItem(studyLocationMarker);
        } else {
            studyLocationMarker.setMapPoint(mapPoint);
        }

        System.out.println("위도 = " + mapPoint.getMapPointGeoCoord().latitude + ", 경도 = " + mapPoint.getMapPointGeoCoord().longitude);
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    // 레퍼런스 상에서 Deprecated 되었다 함.
    @Deprecated
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        // 말풍선 클릭시 지도를 종료하고 이전 액티비티에 위도와 경도값, 주소정보를 돌려주기
        Intent intent;
        MapPoint.GeoCoordinate geoc;

        intent = new Intent();

        geoc = mapPOIItem.getMapPoint().getMapPointGeoCoord();

        intent.putExtra("latitude", geoc.latitude);
        intent.putExtra("longitude", geoc.longitude);

        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}