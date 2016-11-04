package com.smart.cloud.fire.mvp.chat.client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.mvp.chat.common.constant.ConstantValues;
import com.smart.cloud.fire.mvp.chat.common.db.ChatDBManager;
import com.smart.cloud.fire.mvp.chat.common.db.RecentItemDBManager;
import com.smart.cloud.fire.mvp.chat.common.greenDAOBean.ChatBean;
import com.smart.cloud.fire.mvp.chat.common.greenDAOBean.RecentItem;
import com.smart.cloud.fire.mvp.chat.common.utils.FileSaveUtil;
import com.smart.cloud.fire.mvp.chat.common.utils.GetCurrentTime;
import com.smart.cloud.fire.mvp.chat.common.utils.PictureUtil;
import com.smart.cloud.fire.mvp.chat.common.widget.ChatBottomView;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;
import com.smart.cloud.fire.service.LocationService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by Administrator on 2016/11/2.
 */
public class ChatClientPresenter extends BasePresenter<ChatClientView>{
    private LocationService locationService;
    private ChatDBManager chatDBManager;
    private RecentItemDBManager recentItemDBManager;
    private ChatClientActivity chatClientActivity;

    public ChatClientPresenter(ChatClientActivity chatClientActivity, LocationService locationService, ChatDBManager chatDBManager, RecentItemDBManager recentItemDBManager){
        this.locationService = locationService;
        this.chatDBManager = chatDBManager;
        this.recentItemDBManager = recentItemDBManager;
        this.chatClientActivity = chatClientActivity;
        attachView(chatClientActivity);
    }

    public void initLocation(){
        locationService.registerListener(mListener);
    }

    public void startLocation(){
        locationService.start();
    }

    public void stopLocation(){
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                int result = location.getLocType();
                switch (result){
                    case 61:
                        mvpView.getLocationData(location);
                        break;
                    case 161:
                        mvpView.getLocationData(location);
                        break;
                }
                locationService.stop();
            }
        }
    };

    public void openCamera(String camPicPath){
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(new File(camPicPath));
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        chatClientActivity.startActivityForResult(openCameraIntent,
                ChatBottomView.FROM_CAMERA);
    }

    public void openGallery(){
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra("crop", "true");
            intent.putExtra("scale", "true");
            intent.putExtra("scaleUpIfNeeded", true);
        }
        intent.setType("image/*");
        chatClientActivity.startActivityForResult(intent,
                ChatBottomView.FROM_GALLERY);
    }

    public String getSavePicPath() {
        final String dir = FileSaveUtil.SD_CARD_PATH + "image_data/";
        try {
            FileSaveUtil.createSDDirectory(dir);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String fileName = String.valueOf(System.currentTimeMillis() + ".png");
        return dir + fileName;
    }

    /**
     * 发送语音
     */
    public void upVoice(float seconds, String filePath) {
        ChatBean mChatBean = getTbub("13622215086","vidy", ConstantValues.TO_USER_VOICE,
                null, null, null,
                null, filePath, null,seconds, 0);
        chatDBManager.insert(mChatBean);
        mvpView.sendMsgResult(mChatBean);
        uploadTest(filePath);
    }

    /**
     * 接收语音
     */
    float seconds = 0.0f;
    String voiceFilePath = "";

    private void receriveVoiceText(final float seconds, final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                ChatMessageBean tbub = new ChatMessageBean();
//                tbub.setUserName(userName);
//                String time = returnTime();
//                tbub.setTime(time);
//                tbub.setUserVoiceTime(seconds);
//                tbub.setUserVoicePath(filePath);
//                tbAdapter.unReadPosition.add(tblist.size() + "");
//                tbub.setType(ChatListViewAdapter.FROM_USER_VOICE);
//                tblist.add(tbub);
//                sendMessageHandler.sendEmptyMessage(RECERIVE_OK);
//                mChatDbManager.insert(tbub);
            }
        }).start();
    }

    /**
     * 发送文字
     */
    public void sendMsgText(String content) {
        ChatBean mChatBean = getTbub("13622215086","vidy", ConstantValues.TO_USER_MSG,
                content, null, null,
                null, null, null,0, 0);
        boolean result = chatDBManager.insert(mChatBean);
        //发送消息到服务器
        Observable mObservable = apiStores3.pushToSingleServlet("13622215085",content);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<Observable<ResponseBody>>() {
            @Override
            public void onSuccess(Observable<ResponseBody> model) {
            }

            @Override
            public void onFailure(int code, String msg) {
            }
            @Override
            public void onCompleted() {
            }
        }));
        //保存最近一条消息
        RecentItem recentItem = new RecentItem();
        recentItem.setUserId("13622215085");
        recentItem.setName("vidy_lin");
        recentItem.setMsgType(ConstantValues.MESSAGE_TYPE_TEXT);
        recentItem.setMessage(content);
        recentItem.setTime(System.currentTimeMillis());
        recentItemDBManager.insert(recentItem);
        mvpView.sendMsgResult(mChatBean);
    }

    /**
     * 接收文字
     */
    String content = "";

    private void receriveMsgText(final String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String message = "回复：" + content;
//                ChatMessageBean tbub = new ChatMessageBean();
//                tbub.setUserName(userName);
//                String time = returnTime();
//                tbub.setUserContent(message);
//                tbub.setTime(time);
//                tbub.setType(ChatListViewAdapter.FROM_USER_MSG);
//                tblist.add(tbub);
//                sendMessageHandler.sendEmptyMessage(RECERIVE_OK);
//                mChatDbManager.insert(tbub);
            }
        }).start();
    }

    /**
     * 发送图片
     */
    int i = 0;
    public void upImage(String filePath) {
        ChatBean mChatBean = getTbub("13622215086","vidy", ConstantValues.TO_USER_IMG,
                null, filePath, null,
                null, null, null,0f, 0);
        chatDBManager.insert(mChatBean);
        mvpView.sendMsgResult(mChatBean);
        uploadTest(filePath);
    }

    /**
     * 接收图片
     */
    String filePath = "";

    private void receriveImageText(final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                ChatMessageBean tbub = new ChatMessageBean();
//                tbub.setUserName(userName);
//                String time = returnTime();
//                tbub.setTime(time);
//                tbub.setImageLocal(filePath);
//                tbub.setType(ChatListViewAdapter.FROM_USER_IMG);
//                tblist.add(tbub);
//                imageList.add(tblist.get(tblist.size() - 1).getImageLocal());
//                imagePosition.put(tblist.size() - 1, imageList.size() - 1);
//                sendMessageHandler.sendEmptyMessage(RECERIVE_OK);
//                mChatDbManager.insert(tbub);
            }
        }).start();
    }

    public int loadRecords(int number){
        int page = (int) chatDBManager.getPages(number);
        return  page;
    }

    public List<ChatBean> loadPages(int page, int number){
        return chatDBManager.loadPages(page, number);
    }

    private ChatBean getTbub(String userId,String username, int type,
                             String Content, String imageIconUrl, String imageUrl,
                             String imageLocal, String userVoicePath, String userVoiceUrl,
                             float userVoiceTime, int sendState) {
        ChatBean tbub = new ChatBean();
        tbub.setUserId(userId);
        tbub.setUserName(username);
        String time = GetCurrentTime.returnTime();
        tbub.setTime(time);
        tbub.setType(type);
        tbub.setUserContent(Content);
        tbub.setImageIconUrl(imageIconUrl);
        tbub.setImageUrl(imageUrl);
        tbub.setUserVoicePath(userVoicePath);
        tbub.setUserVoiceUrl(userVoiceUrl);
        tbub.setUserVoiceTime(userVoiceTime);
        tbub.setSendState(sendState);
        tbub.setImageLocal(imageLocal);
        return tbub;
    }

    public void showDialog(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // // TODO Auto-generated method stub
                try {
                    String GalPicPath = getSavePicPath();
                    Bitmap bitmap = PictureUtil.compressSizeImage(path);
                    boolean isSave = FileSaveUtil.saveBitmap(
                            PictureUtil.reviewPicRotate(bitmap, GalPicPath),
                            GalPicPath);
                    File file = new File(GalPicPath);
                    if (file.exists() && isSave) {
                        upImage(GalPicPath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void uploadTest(String fileUrl) {
        File file1 = new File(fileUrl);
        // 获取文件真实的minetype
        Map<String, RequestBody> params = new HashMap<>();
        String mimeType = MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(
                        MimeTypeMap.getFileExtensionFromUrl(file1.getPath()));
//        String mimeType2 = MimeTypeMap.getSingleton()
//                .getMimeTypeFromExtension(
//                        MimeTypeMap.getFileExtensionFromUrl(file1.getPath()));
        // 网上看了大量的都是传的image/png,或者image/jpg 啥的，这个参数还不是很明白，需要跟下源码在研究下。用这个minetype文件能上传成功。
        RequestBody fileBody = RequestBody.create(MediaType.parse(mimeType), file1);
//        RequestBody fileBody2 = RequestBody.create(MediaType.parse(mimeType2), file1);
        params.put("file\"; filename=\"" + file1.getName() + "", fileBody);
//        params.put("file\"; filename=\"" + file1.getName() + "", fileBody2);
        Observable mObservable = apiStores3.upload(params,"江苏旷达分开","图片1");
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                try {
                    String jsonString = new String(response.body().bytes()); // 这就是返回的json字符串了。
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
            }

            @Override
            public void onCompleted() {
            }
        }));
    }

    public void downloadFile(String fileUrl){
        Observable mObservable = apiStores3.downloadFileWithDynamicUrlSync(fileUrl);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                if (response.isSuccess()) {
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                }
            }

            @Override
            public void onFailure(int code, String msg) {
            }

            @Override
            public void onCompleted() {
            }
        }));
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(Environment.getExternalStorageDirectory().getCanonicalFile()
                    + File.separator + "Future Studio Icon.png");
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
