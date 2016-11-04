package com.smart.cloud.fire.mvp.chat.client;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.AppComponent;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.chat.common.adapter.ChatListViewAdapter;
import com.smart.cloud.fire.mvp.chat.common.adapter.DataAdapter;
import com.smart.cloud.fire.mvp.chat.common.constant.ConstantValues;
import com.smart.cloud.fire.mvp.chat.common.greenDAOBean.ChatBean;
import com.smart.cloud.fire.mvp.chat.common.utils.FileSaveUtil;
import com.smart.cloud.fire.mvp.chat.common.utils.ImageCheckoutUtil;
import com.smart.cloud.fire.mvp.chat.common.utils.KeyBoardUtils;
import com.smart.cloud.fire.mvp.chat.common.widget.AudioRecordButton;
import com.smart.cloud.fire.mvp.chat.common.widget.ChatBottomView;
import com.smart.cloud.fire.mvp.chat.common.widget.HeadIconSelectorView;
import com.smart.cloud.fire.mvp.chat.common.widget.PullToRefreshLayout;
import com.smart.cloud.fire.mvp.chat.common.widget.PullToRefreshListView;
import com.smart.cloud.fire.mvp.chat.common.widget.PullToRefreshView;
import com.smart.cloud.fire.mvp.chat.common.widget.WrapContentLinearLayoutManager;
import com.smart.cloud.fire.utils.SharedPreferencesManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/11/2.
 */
public class ChatClientActivity extends MvpActivity<ChatClientPresenter> implements ChatClientView {
    @Inject
    ChatClientPresenter chatClientPresenter;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.title_rela)
    RelativeLayout titleRela;
    @Bind(R.id.bd_map_View)
    MapView bdMapView;
    @Bind(R.id.local_tv)
    TextView localTv;
    @Bind(R.id.send_local)
    TextView sendLocal;
    @Bind(R.id.add_fire)
    ImageView addFire;
    @Bind(R.id.content_lv)
    PullToRefreshLayout contentLv;
    @Bind(R.id.voice_iv)
    ImageView voiceIv;
    @Bind(R.id.mess_et)
    EditText messEt;
    @Bind(R.id.voice_btn)
    AudioRecordButton voiceBtn;
    @Bind(R.id.emoji)
    ImageView emoji;
    @Bind(R.id.mess_iv)
    ImageView messIv;
    @Bind(R.id.tongbao_utils)
    LinearLayout tongbaoUtils;
    @Bind(R.id.vPager)
    ViewPager vPager;
    @Bind(R.id.send_emoji_icon)
    TextView sendEmojiIcon;
    @Bind(R.id.emoji_group)
    LinearLayout emojiGroup;
    @Bind(R.id.other_lv)
    ChatBottomView otherLv;
    @Bind(R.id.mess_lv)
    ListView messLv;
    @Bind(R.id.bottom_container_ll)
    LinearLayout bottomContainerLl;
    private BaiduMap mBaiduMap;
    private Context mContext;

    public PullToRefreshListView myList;
    private String item[] = {"你好!", "我正忙着呢,等等", "有啥事吗？", "有时间聊聊吗", "再见！"};
    private DataAdapter adapter;
    public ChatListViewAdapter tbAdapter;
    private boolean CAN_WRITE_EXTERNAL_STORAGE = true;
    private String camPicPath;
    private List<ChatBean> tblist = new ArrayList<>();
    private WrapContentLinearLayoutManager wcLinearLayoutManger;
    private static final int IMAGE_SIZE = 100 * 1024;
    private File mCurrentPhotoFile;
    private List<ChatBean> pagelist = new ArrayList<>();
    private ArrayList<String> imageList = new ArrayList<>();//adapter图片数据
    private HashMap<Integer, Integer> imagePosition = new HashMap<>();//图片下标位置
    private boolean isDown = false;
    private int page = 0;
    private int number = 10;
    private int position;
    private String userID;
    private int privilege;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        ButterKnife.bind(this);
        mContext = this;
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        setupActivityComponent(MyApp.get(this).getAppComponent());
        chatClientPresenter.startLocation();
        init();
        initView();
    }

    private void initView(){
        contentLv.setSlideView(new PullToRefreshView(this).getSlideView(PullToRefreshView.LISTVIEW));
        myList = (PullToRefreshListView) contentLv.returnMylist();
        myList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
        adapter = new DataAdapter(this, item);
        messLv.setAdapter(adapter);
        tbAdapter = new ChatListViewAdapter(this);
        tbAdapter.setUserList(tblist);
        myList.setAdapter(tbAdapter);

        wcLinearLayoutManger = new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myList.setAdapter(tbAdapter);

        messEt.setOnKeyListener(onKeyListener);
        voiceBtn.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onStart() {
                tbAdapter.stopPlayVoice();
            }

            @Override
            public void onFinished(float seconds, String filePath) {
                chatClientPresenter.upVoice(seconds, filePath);
            }
        });
        otherLv.setOnHeadIconClickListener(new HeadIconSelectorView.OnHeadIconClickListener() {
            @Override
            public void onClick(int from) {
                switch (from){
                    case ChatBottomView.FROM_CAMERA:
                        if(!CAN_WRITE_EXTERNAL_STORAGE){
                            Toast.makeText(mContext,"权限未开通\n请到设置中开通相册权限",Toast.LENGTH_SHORT).show();
                        }else {
                            final String state = Environment.getExternalStorageState();
                            if (Environment.MEDIA_MOUNTED.equals(state)) {
                                camPicPath = chatClientPresenter.getSavePicPath();
                                chatClientPresenter.openCamera(camPicPath);
                            } else {
                                Toast.makeText(mActivity,"请检查内存卡",Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case ChatBottomView.FROM_GALLERY:
                        if(!CAN_WRITE_EXTERNAL_STORAGE){
                            Toast.makeText(mContext,"权限未开通\n请到设置中开通相册权限",Toast.LENGTH_SHORT).show();
                        }else {
                            String status = Environment.getExternalStorageState();
                            if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
                                chatClientPresenter.openGallery();
                            } else {
                                Toast.makeText(mActivity,"没有SD卡",Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case ChatBottomView.FROM_PHRASE:
                        if (messLv.getVisibility() == View.GONE) {
                            otherLv.setVisibility(View.GONE);
                            emoji.setBackgroundResource(R.mipmap.emoji);
                            voiceIv.setBackgroundResource(R.mipmap.voice_btn_normal);
                            messLv.setVisibility(View.VISIBLE);
                            KeyBoardUtils.hideKeyBoard(mContext,
                                    messEt);
                            messIv.setBackgroundResource(R.mipmap.chatting_setmode_keyboard_btn_normal);
                        }
                }
            }
        });

        myList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        tbAdapter.handler.removeCallbacksAndMessages(null);
                        tbAdapter.setIsGif(true);
                        tbAdapter.isPicRefresh = false;
                        tbAdapter.notifyDataSetChanged();
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        tbAdapter.handler.removeCallbacksAndMessages(null);
                        tbAdapter.setIsGif(false);
                        tbAdapter.isPicRefresh = true;
                        reset();
                        KeyBoardUtils.hideKeyBoard(mContext,
                                messEt);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        page = chatClientPresenter.loadRecords(number);
        loadRecords();
    }

    private void loadRecords() {
        isDown = true;
        if (pagelist != null) {
            pagelist.clear();
        }
        pagelist = chatClientPresenter.loadPages(page,number);
        position = pagelist.size();
        if (pagelist.size() != 0) {
            pagelist.addAll(tblist);
            tblist.clear();
            tblist.addAll(pagelist);
            if (imageList != null) {
                imageList.clear();
            }
            if (imagePosition != null) {
                imagePosition.clear();
            }
            int key = 0;
            int position = 0;
            for (ChatBean cmb : tblist) {
                if (cmb.getType() == ConstantValues.FROM_USER_IMG || cmb.getType() == ConstantValues.TO_USER_IMG) {
                    imageList.add(cmb.getImageLocal());
                    imagePosition.put(key, position);
                    position++;
                }
                key++;
            }
            tbAdapter.setImageList(imageList);
            tbAdapter.setImagePosition(imagePosition);
            contentLv.refreshComplete();
            tbAdapter.notifyDataSetChanged();
            myList.setSelection(position - 1);
            isDown = false;
            if (page == 0) {
                contentLv.refreshComplete();
                contentLv.setPullGone();
            } else {
                page--;
            }
        } else {
            if (page == 0) {
                contentLv.refreshComplete();
                contentLv.setPullGone();
            }
        }
    }

    private void init() {
        mBaiduMap = bdMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
        bdMapView.setLongClickable(true);
        // 隐藏百度logo
        bdMapView.removeViewAt(1);
        // 隐藏比例尺
        bdMapView.showScaleControl(false);
        // 隐藏缩放控件
        bdMapView.showZoomControls(false);
    }

    @OnItemClick({R.id.mess_lv})
    public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                            long arg3){
        String msg = item[arg2];
        chatClientPresenter.sendMsgText(msg);
    }

    @OnClick({R.id.mess_iv,R.id.voice_iv,R.id.mess_et})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.mess_iv:
                emojiGroup.setVisibility(View.GONE);
                if (otherLv.getVisibility() == View.GONE
                        && messLv.getVisibility() == View.GONE) {
                    messEt.setVisibility(View.VISIBLE);
                    messIv.setFocusable(true);
                    voiceBtn.setVisibility(View.GONE);
                    emoji.setBackgroundResource(R.mipmap.emoji);
                    voiceIv.setBackgroundResource(R.mipmap.voice_btn_normal);
                    otherLv.setVisibility(View.VISIBLE);
                    KeyBoardUtils.hideKeyBoard(mContext,
                            messEt);
                    messIv.setBackgroundResource(R.mipmap.chatting_setmode_keyboard_btn_normal);
                } else {
                    otherLv.setVisibility(View.GONE);
                    KeyBoardUtils.showKeyBoard(mContext, messEt);
                    messIv.setBackgroundResource(R.mipmap.tb_more);
                    if (messIv.getVisibility() != View.GONE) {
//                        messIv.setVisibility(View.GONE);
                        KeyBoardUtils.showKeyBoard(mContext, messEt);
                        messIv.setBackgroundResource(R.mipmap.tb_more);
                    }
                }
                break;
            case R.id.voice_iv:
                if (voiceBtn.getVisibility() == View.GONE) {
                    emoji.setBackgroundResource(R.mipmap.emoji);
                    messIv.setBackgroundResource(R.mipmap.tb_more);
                    messEt.setVisibility(View.GONE);
                    emojiGroup.setVisibility(View.GONE);
                    otherLv.setVisibility(View.GONE);
                    messLv.setVisibility(View.GONE);
                    voiceBtn.setVisibility(View.VISIBLE);
                    KeyBoardUtils.hideKeyBoard(mContext,
                            messEt);
                    voiceIv.setBackgroundResource(R.mipmap.chatting_setmode_keyboard_btn_normal);
                } else {
                    messEt.setVisibility(View.VISIBLE);
                    voiceBtn.setVisibility(View.GONE);
                    voiceIv.setBackgroundResource(R.mipmap.voice_btn_normal);
                    KeyBoardUtils.showKeyBoard(mContext, messEt);
                }
                break;
            case R.id.mess_et:
                otherLv.setVisibility(View.GONE);
                messLv.setVisibility(View.GONE);
                messIv.setBackgroundResource(R.mipmap.tb_more);
                voiceIv.setBackgroundResource(R.mipmap.voice_btn_normal);
                break;
            default:
                break;
        }
    }

    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerChatClientComponent.builder()
                .appComponent(appComponent)
                .chatClientModule(new ChatClientModule(this))
                .build()
                .injectChatClientActivity(this);
    }

    /**
     * 界面复位
     */
    protected void reset() {
        otherLv.setVisibility(View.GONE);
        messLv.setVisibility(View.GONE);
        emoji.setBackgroundResource(R.mipmap.emoji);
        messIv.setBackgroundResource(R.mipmap.tb_more);
        voiceIv.setBackgroundResource(R.mipmap.voice_btn_normal);
    }

    //发送文字
    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                String content = messEt.getText().toString();
                chatClientPresenter.sendMsgText(content);
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            otherLv.setVisibility(View.GONE);
            messIv.setBackgroundResource(R.mipmap.tb_more);
            switch (requestCode) {
                case ChatBottomView.FROM_CAMERA:
                    FileInputStream is = null;
                    try {
                        is = new FileInputStream(camPicPath);
                        File camFile = new File(camPicPath); // 图片文件路径
                        if (camFile.exists()) {
                            int size = ImageCheckoutUtil.getImageSize(ChatListViewAdapter.getLoacalBitmap(camPicPath));
                            if (size > IMAGE_SIZE) {
                                chatClientPresenter.showDialog(camPicPath);
                            } else {
                                chatClientPresenter.upImage(camPicPath);
                            }
                        } else {
                            Toast.makeText(mActivity,"该文件不存在!",Toast.LENGTH_SHORT).show();
                        }
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        // 关闭流
                        try {
                            is.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
                case ChatBottomView.FROM_GALLERY:
                    Uri uri = data.getData();
                    String path = FileSaveUtil.getPath(getApplicationContext(), uri);
                    mCurrentPhotoFile = new File(path); // 图片文件路径
                    if (mCurrentPhotoFile.exists()) {
                        int size = ImageCheckoutUtil.getImageSize(ChatListViewAdapter
                                .getLoacalBitmap(path));
                        if (size > IMAGE_SIZE) {
                            chatClientPresenter.showDialog(path);
                        } else {
                            chatClientPresenter.upImage(path);
                        }
                    } else {
                        Toast.makeText(mActivity,"该文件不存在!",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            // Toast.makeText(this, "操作取消", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected ChatClientPresenter createPresenter() {
        return null;
    }


    @Override
    public void sendMsgResult(ChatBean mChatBean) {
        tblist.add(mChatBean);
        messEt.setText("");
        tbAdapter.isPicRefresh = true;
        tbAdapter.notifyDataSetChanged();
        myList.setSelection(tblist.size() - 1);
    }

    @Override
    public void getLocationData(BDLocation location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        LatLng p = new LatLng(latitude, longitude);
        bdMapView = new MapView(this,
                new BaiduMapOptions().mapStatus(new MapStatus.Builder()
                        .target(p).build()));
        String address = location.getAddrStr();
        showMap(latitude, longitude, address);
    }

    private void showMap(double latitude, double longitude, String address) {
        localTv.setText(address);
        View viewA = LayoutInflater.from(mContext).inflate(
                R.layout.image_mark, null);
        LatLng llA = new LatLng(latitude, longitude);
        OverlayOptions ooA = new MarkerOptions().position(llA).icon(BitmapDescriptorFactory
                .fromView(viewA))
                .zIndex(4).draggable(true);
        mBaiduMap.addOverlay(ooA);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(llA, 17.0f);
        mBaiduMap.animateMapStatus(u);
    }

    @Override
    public void onStart() {
        chatClientPresenter.initLocation();
        super.onStart();
    }

    @Override
    public void onDestroy() {
        chatClientPresenter.stopLocation();
        super.onDestroy();
    }
}
