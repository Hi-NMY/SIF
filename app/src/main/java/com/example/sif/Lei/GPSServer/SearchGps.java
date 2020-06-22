package com.example.sif.Lei.GPSServer;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.sif.Lei.MyBroadcastReceiver.BroadcastRec;
import com.example.sif.Lei.MyToolClass.ToastZong;
import com.example.sif.MyApplication;
import com.example.sif.NeiBuLei.MsgGpsClass;

import java.util.ArrayList;
import java.util.List;

public class SearchGps {

    private static String city;
    private static String search;
    private static SuggestionSearch suggestionSearch;
    public static List<MsgGpsClass> searchMsgGps;
    private static MsgGpsClass msgGpsClass;
    public static void Search(String c,String s){
        city = c;
        search = s;
        suggestionSearch = SuggestionSearch.newInstance();
        SuggestionSearchOption suggestionSearchOption = new SuggestionSearchOption();
        suggestionSearchOption.mCityLimit = true;
        suggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                searchMsgGps = new ArrayList<>();
                try {
                    if (suggestionResult != null && suggestionResult.getAllSuggestions().size() != 0){
                        for (int j = 0 ; j < suggestionResult.getAllSuggestions().size() ; j++){
                            msgGpsClass = new MsgGpsClass();
                            msgGpsClass.setPlaceName(suggestionResult.getAllSuggestions().get(j).getKey());
                            msgGpsClass.setPlaceDetailed(suggestionResult.getAllSuggestions().get(j).getCity());
                            searchMsgGps.add(msgGpsClass);
                        }
                        BroadcastRec.sendReceiver(MyApplication.getContext(),"nearbyPlace",2,"");
                    }
                }catch (Exception e){
                    ToastZong.ShowToast(MyApplication.getContext(),"太快了，慢一点");
                }
            }
        });

        suggestionSearch.requestSuggestion(new SuggestionSearchOption()
                .city(city)
                .keyword(search));
    }

}
