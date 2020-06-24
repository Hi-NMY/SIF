package com.example.sif.Lei.LianJie;

import android.text.TextUtils;
import com.example.sif.NeiBuLei.DouBleImagePath;
import okhttp3.*;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HttpUtil {
    public static void sendOkHttpPost(String path,String xuehao,okhttp3.Callback callback) {
        RequestBody requestBody = new FormBody.Builder()
                .add("xuehao", xuehao)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void cunGuangChangMessage(int id, String path, List<DouBleImagePath> imagePath, String userName, String messageShiJian, String message, String xuehao, int thumb, int comment, StringBuffer block,String place,String ip, okhttp3.Callback callback){
        RequestBody requestBody = null;
        if (id == 1){
//           File file = new File(imagePath);
//           File file1 = new File(imageApath);
           MultipartBody.Builder builder = new MultipartBody.Builder();
                   builder.setType(MultipartBody.FORM)
                   .addFormDataPart("id", String.valueOf(id))
                   .addFormDataPart("userName",userName)
                   .addFormDataPart("messageTime",messageShiJian)
                   .addFormDataPart("messageContent",message)
                   .addFormDataPart("thumb",String.valueOf(thumb))
                   .addFormDataPart("comment",String.valueOf(comment))
                   .addFormDataPart("xuehao",xuehao)
                    .addFormDataPart("place",place)
                   .addFormDataPart("block",String.valueOf(block));
            for(DouBleImagePath d : imagePath){
                File file = new File(d.getMinPath());
                File file1 = new File(d.getMaxPath());
                builder.addFormDataPart("img[]",file.getName(),RequestBody.create(MediaType.parse("image/png"),file))
                        .addFormDataPart("imgA[]",file1.getName(),RequestBody.create(MediaType.parse("image/png"),file1));
            }
           requestBody = builder.build();
       }
       if (id == 0){
           requestBody = new FormBody.Builder()
                   .add("id", String.valueOf(id))
                   .add("userName",userName)
                   .add("messageTime",messageShiJian)
                   .add("messageContent",message)
                   .add("xuehao",xuehao)
                   .add("place",place)
                   .add("thumb", String.valueOf(thumb))
                   .add("comment",String.valueOf(comment))
                   .add("block",String.valueOf(block))
                   .build();
       }
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20,TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void changeInformation(String path,int id,String message,String oldName,String xuehao,okhttp3.Callback callback){
        if (id == 1){
            RequestBody requestBody = new FormBody.Builder()
                    .add("id", String.valueOf(id))
                    .add("xuehao",xuehao)
                    .add("text",message)
                    .add("oldName",oldName)
                    .build();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20,TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url(path)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(callback);
        }
        if (id == 2){
            RequestBody requestBody = new FormBody.Builder()
                    .add("xuehao",xuehao)
                    .add("text",message)
                    .add("id", String.valueOf(id))
                    .build();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20,TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url(path)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(callback);
        }
        if (id == 3){
            RequestBody requestBody = new FormBody.Builder()
                    .add("xuehao",xuehao)
                    .add("text",message)
                    .add("id", String.valueOf(id))
                    .build();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20,TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url(path)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(callback);
        }
    }

    public static void addGuangChangXin(String path,String nowDate,okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("nowTime",nowDate)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void addUserSpace(String path,String nowTime,String xuehao,okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("xuehao",xuehao)
                .add("nowTime",nowTime)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteDynamic(String path,String dynamicid,String xuehao,okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("xuehao",xuehao)
                .add("id",dynamicid)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void dianThumb(String path,int id,String dynamicid,String xuehao,okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .add("xuehao",xuehao)
                .add("dynamicid",dynamicid)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void cunThumb(String path,int id,String dynamicid,String xuehao,okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .add("xuehao",xuehao)
                .add("dynamicid",dynamicid)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void Thumb(String path,String xuehao,okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("xuehao",xuehao)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendMyHeadImage(String path,int id,String xuehao,String url, okhttp3.Callback callback){
        File file = new File(url);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", String.valueOf(id))
                .addFormDataPart("xuehao",xuehao)
                .addFormDataPart("img",file.getName(),RequestBody.create(MediaType.parse("image/png"),file));
        RequestBody requestBody = builder.build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void dynamicDetailedObtain(String path,String xuehao,String dynamicid, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("xuehao", xuehao)
                .add("dynamicid",dynamicid)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void dynamicDetailedComment(String path,int id,String dynamicid,String nowTime, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .add("dynamicid",dynamicid)
                .add("nowTime",nowTime)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendComment(String path,int i,String dynamicid,String dynamiccomment,String commenttime,String commentusername,String tousername,
                                   String userheadimageurl,String commentxuehao,String userip,String userxuehao,int userid,okhttp3.Callback callback){
        RequestBody requestBody;
        if (TextUtils.isEmpty(tousername)){
            requestBody = new FormBody.Builder()
                    .add("id", String.valueOf(i))
                    .add("dynamicid",dynamicid)
                    .add("dynamiccomment",dynamiccomment)
                    .add("commenttime",commenttime)
                    .add("commentusername",commentusername)
                    .add("userheadimageurl",userheadimageurl)
                    .add("commentxuehao",commentxuehao)
                    .add("userxuehao",userxuehao)
                    .add("userid", String.valueOf(userid))
                    .build();
        }else {
            requestBody = new FormBody.Builder()
                    .add("id", String.valueOf(i))
                    .add("dynamicid",dynamicid)
                    .add("dynamiccomment",dynamiccomment)
                    .add("commenttime",commenttime)
                    .add("commentusername",commentusername)
                    .add("tousername",tousername)
                    .add("userheadimageurl",userheadimageurl)
                    .add("commentxuehao",commentxuehao)
                    .add("userxuehao",userxuehao)
                    .add("userid", String.valueOf(userid))
                    .build();
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void dynamicCollection(String path,int id,String dynamicid,String headimage,String xuehao,String username,String message,String image,String myxuehao, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .add("dynamicid",dynamicid)
                .add("headimage",headimage)
                .add("xuehao",xuehao)
                .add("username",username)
                .add("message",message)
                .add("image",image)
                .add("myxuehao",myxuehao)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void dynamicCollection(String path,int id,String dynamicid,String myxuehao, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .add("dynamicid",dynamicid)
                .add("myxuehao",myxuehao)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void inquireCollection(String path,String xuehao,int id, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .add("xuehao",xuehao)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void newDynamicMessage(String path,String xuehao, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("xuehao",xuehao)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendUserIP(String path,String xuehao,String ip, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("xuehao",xuehao)
                .add("ip",ip)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendGeTuiMessage(String path,int i,String xuehao,String sendxuehao,String ip,String dynamicid,int fun, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(i))
                .add("xuehao",xuehao)
                .add("sendXueHao",sendxuehao)
                .add("ip",ip)
                .add("dynamicId",dynamicid)
                .add("fun", String.valueOf(fun))
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void myFollow(String path,int i,String xuehao,String userxuehao, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(i))
                .add("xuehao",xuehao)
                .add("userxuehao",userxuehao)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void followDynamic(String path,StringBuffer follow,String nowTime, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("follow", String.valueOf(follow))
                .add("nowTime",nowTime)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void ibsend(String path,int id,String blockname, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .add("blockname",blockname)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void blockLabel(String path,int id,String blockname, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .add("blockname", blockname)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void blockChange(String path,StringBuffer block, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("block", String.valueOf(block))
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void blockSend(String path,String blockname,int id,String time, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("blockname", blockname)
                .add("id", String.valueOf(id))
                .add("nowTime",time)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void myIbFollow(String path,int i,String xuehao,String blockname, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(i))
                .add("xuehao",xuehao)
                .add("blockname",blockname)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void followIb(String path,StringBuffer follow,int id, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("follow", String.valueOf(follow))
                .add("id",String.valueOf(id))
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendVoice(String path,String voicepath,String userName,String usersex,String label,String xuehao,okhttp3.Callback callback){
        RequestBody requestBody = null;
        File file = new File(voicepath);
        MultipartBody.Builder builder = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("userName",userName)
            .addFormDataPart("usersex",usersex)
            .addFormDataPart("label",label)
            .addFormDataPart("xuehao",xuehao)
            .addFormDataPart("voice",file.getName(),RequestBody.create(MediaType.parse("audio/m4a"),file));
        requestBody = builder.build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20,TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void obtainVoice(String path,int fun,int id, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("fun", String.valueOf(fun))
                .add("id",String.valueOf(id))
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void newPassword(String path,String xuehao,String oldPass,String newPass, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("xuehao", String.valueOf(xuehao))
                .add("oldPass", String.valueOf(oldPass))
                .add("newPass",String.valueOf(newPass))
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void myPrivacy(String path,String xuehao,String privacy, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("xuehao", String.valueOf(xuehao))
                .add("privacy", privacy)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendSecret(String path,int fun,String xuehao,String secret, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("fun",String.valueOf(fun))
                .add("xuehao", xuehao)
                .add("secret", secret)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void obtainTimeTable(String path,String zhuanye,String banji,String nianji,String nextDate, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("zhuanye",zhuanye)
                .add("banji", banji)
                .add("nianji", nianji)
                .add("nextDate", nextDate)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendUserDiary(int id,String path,String xuehao,int weathernum,String messagediary,String diarydate,String imagepath,String imagepath1,okhttp3.Callback callback){
        RequestBody requestBody = null;
        if (id == 1){
            File file = new File(imagepath);
            File file1 = new File(imagepath1);
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("id", String.valueOf(id))
                    .addFormDataPart("weathernum",String.valueOf(weathernum))
                    .addFormDataPart("messagediary",messagediary)
                    .addFormDataPart("diarydate",diarydate)
                    .addFormDataPart("xuehao",xuehao)
                    .addFormDataPart("img",file.getName(),RequestBody.create(MediaType.parse("image/png"),file))
                    .addFormDataPart("imgA",file1.getName(),RequestBody.create(MediaType.parse("image/png"),file1));
            requestBody = builder.build();
        }
        if (id == 0){
            requestBody = new FormBody.Builder()
                    .add("id", String.valueOf(id))
                    .add("weathernum",String.valueOf(weathernum))
                    .add("messagediary",messagediary)
                    .add("diarydate",diarydate)
                    .add("xuehao",xuehao)
                    .build();
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20,TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void wrightDiary(String path,int fun,int id,String xuehao,String diarydate, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("fun",String.valueOf(fun))
                .add("id", String.valueOf(id))
                .add("diarydate", diarydate)
                .add("xuehao", xuehao)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void rongCloudServer(String path,String id,String xuehao,String userName, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .add("xuehao", xuehao)
                .add("userName", userName)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendUserToken(String path,String token,String xuehao, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("token", token)
                .add("xuehao", xuehao)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendAddNewShop(String path, List<DouBleImagePath> imagePath, String xuehao, String sendtime, StringBuffer label, String notice, int shopstate, Callback callback){
        RequestBody requestBody = null;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("xuehao",xuehao)
                .addFormDataPart("sendtime",sendtime)
                .addFormDataPart("label", String.valueOf(label))
                .addFormDataPart("notice",notice)
                .addFormDataPart("shopstate",String.valueOf(shopstate));
        for(DouBleImagePath d : imagePath){
            File file = new File(d.getMinPath());
            File file1 = new File(d.getMaxPath());
            builder.addFormDataPart("img[]",file.getName(),RequestBody.create(MediaType.parse("image/png"),file))
                    .addFormDataPart("imgA[]",file1.getName(),RequestBody.create(MediaType.parse("image/png"),file1));
        }
        requestBody = builder.build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20,TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void obtainSchoolShop(String path,String nowTime, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("nowTime", nowTime)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void obtainUserName(String path,String xuehao, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("xuehao", xuehao)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void obtainUserShop(String path,String xuehao, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("xuehao", xuehao)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void updateShopState(String path,int id,String xuehao,int state, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("id",String.valueOf(id))
                .add("xuehao", xuehao)
                .add("state",String.valueOf(state))
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void searchSchoolShop(String path,String keyword, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("keyword", keyword)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void obtainSign(String path,String xuehao, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("xuehao", xuehao)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void updateSign(String path,int signday,String xuehao,int coinSize, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("xuehao", xuehao)
                .add("signday",String.valueOf(signday))
                .add("coinsize",String.valueOf(coinSize))
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void patchSign(String path,int signday,String xuehao,int coinSize, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("xuehao", xuehao)
                .add("signday",String.valueOf(signday))
                .add("coinSize",String.valueOf(coinSize))
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void updateTaskCoin(String path,String xuehao,int coinSize, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("xuehao", xuehao)
                .add("coinsize",String.valueOf(coinSize))
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void obtainUserLongDay(String path,String xuehao, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("xuehao", xuehao)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void obtainUser(String path,String key, okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("key", key)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    private void sendOutInternet(String path){

    }

    private void sendOutInternet(String path,RequestBody requestBody){

    }

}
