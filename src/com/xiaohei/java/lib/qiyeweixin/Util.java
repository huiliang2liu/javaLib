package com.xiaohei.java.lib.qiyeweixin;

import com.xiaohei.java.lib.http.HttpManage;
import com.xiaohei.java.lib.http.request.FileUploadRequest;
import com.xiaohei.java.lib.http.request.FlowRequest;
import com.xiaohei.java.lib.http.request.Request;
import com.xiaohei.java.lib.http.response.Response;
import com.xiaohei.java.lib.http.util.Method;
import com.xiaohei.java.lib.json.JSONException;
import com.xiaohei.java.lib.json.JSONObject;

import java.io.File;

public class Util {
    //接入文档地址：https://qydev.weixin.qq.com/wiki/index.php?title=%E4%B8%BB%E5%8A%A8%E8%B0%83%E7%94%A8
    private static final String GET_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    private static final String SEND_MSG = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";
    private static final String PUSH_FILE = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=%s&type=%s";
    private static final String MSG_TEMPLE = "{\"%s\": \"%s\",\"msgtype\": \"%s\",\"agentid\": %s,%s,\"safe\":0}";
    private static final String TEXT_MSG = "\"text\":{\"content\":\"%s\"}";
    private static final String IMAGE_MSG = "\"image\":{\"media_id\":\"%s\"}";
    private static final String VOICE_MSG = "\"voice\":{\"media_id\":\"%s\"}";
    private static final HttpManage manage = new HttpManage();


    public static String getToken(String id, String secrect) {
        Request request = new Request();
        request.setPath(String.format(GET_TOKEN, id, secrect));
        Response response = manage.response(request);
        String res = response.getString();
        try {
            JSONObject jsonObject = new JSONObject(res);
            if (jsonObject.has("access_token"))
                return jsonObject.getString("access_token");
            System.out.println(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    private static String sendMessage(String token, String msg) {
        System.out.println(msg);
        FlowRequest request = new FlowRequest(msg);
        request.setPath(String.format(SEND_MSG, token));
        request.setMethod(Method.POST);
        request.setContentType("application/json");
        Response response = manage.response(request);
        String res = response.getString();
        try {
            JSONObject jsonObject = new JSONObject(res);
            int errcode = jsonObject.optInt("errcode", -1);
            if (errcode == 0)
                return "发送成功";
            System.out.println(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String createMsg(String toAtt, String toName, String msgType, String id, String text) {
        return String.format(MSG_TEMPLE, toAtt, toName, msgType, id, text);
    }

    private static String createTextMsg(String toAtt, String toName, String id, String text) {
        return createMsg(toAtt, toName, "text", id, text);
    }

    private static String createImageMsg(String toAtt, String toName, String id, String text) {
        return createMsg(toAtt, toName, "image", id, text);
    }

    private static String createVoiceMsg(String toAtt, String toName, String id, String text) {
        return createMsg(toAtt, toName, "voice", id, text);
    }

    private static String createImageMsgToUser(String user, String agentid, String msg) {
        return createImageMsg("touser", user, agentid, String.format(IMAGE_MSG, msg));
    }

    private static String createImageMsgToParty(String party, String agentid, String msg) {
        return createImageMsg("toparty", party, agentid, String.format(IMAGE_MSG, msg));
    }

    private static String createImageMsgToTag(String tag, String agentid, String msg) {
        return createImageMsg("totag", tag, agentid, String.format(IMAGE_MSG, msg));
    }

    private static String createVoiceMsgToUser(String user, String agentid, String msg) {
        return createVoiceMsg("touser", user, agentid, String.format(VOICE_MSG, msg));
    }

    private static String createVoiceMsgToParty(String party, String agentid, String msg) {
        return createVoiceMsg("toparty", party, agentid, String.format(VOICE_MSG, msg));
    }

    private static String createVoiceMsgToTag(String tag, String agentid, String msg) {
        return createVoiceMsg("totag", tag, agentid, String.format(VOICE_MSG, msg));
    }

    private static String createTextMsgToUser(String user, String agentid, String msg) {
        return createTextMsg("touser", user, agentid, String.format(TEXT_MSG, msg));
    }

    private static String createTextMsgToParty(String party, String agentid, String msg) {
        return createTextMsg("toparty", party, agentid, String.format(TEXT_MSG, msg));
    }

    private static String createTextMsgToTag(String tag, String agentid, String msg) {
        return createTextMsg("totag", tag, agentid, String.format(TEXT_MSG, msg));
    }

    public static String sendTextMsgToUser(String token, String user, String agentid, String msg) {
        return sendMessage(token, createTextMsgToUser(user, agentid, msg));
    }

    public static String sendTextMsgToParty(String token, String party, String agentid, String msg) {
        return sendMessage(token, createTextMsgToParty(party, agentid, msg));
    }

    public static String sendTextMsgToTag(String token, String tag, String agentid, String msg) {
        return sendMessage(token, createTextMsgToTag(tag, agentid, msg));
    }

    public static String sendImageMsgToUser(String token, String user, String agentid, String msg) {
        return sendMessage(token, createImageMsgToUser(user, agentid, msg));
    }

    public static String sendImageMsgToParty(String token, String party, String agentid, String msg) {
        return sendMessage(token, createImageMsgToParty(party, agentid, msg));
    }

    public static String sendImageMsgToTag(String token, String tag, String agentid, String msg) {
        return sendMessage(token, createImageMsgToTag(tag, agentid, msg));
    }

    public static String sendVoiceMsgToUser(String token, String user, String agentid, String msg) {
        return sendMessage(token, createVoiceMsgToUser(user, agentid, msg));
    }

    public static String sendVoiceMsgToParty(String token, String party, String agentid, String msg) {
        return sendMessage(token, createVoiceMsgToParty(party, agentid, msg));
    }

    public static String sendVoiceMsgToTag(String token, String tag, String agentid, String msg) {
        return sendMessage(token, createVoiceMsgToTag(tag, agentid, msg));
    }

    public static String pushFile(String token, String type, File file) {
        FileUploadRequest request = new FileUploadRequest(file);
        request.setFilename(file.getName());
        request.setFileKey("file");
        request.setPath(String.format(PUSH_FILE, token, type));
        return manage.response(request).getString();
    }

    public static String pushImage(String token, File file) {
        return pushFile(token, "image", file);
    }

    public static String pushVoice(String token, File file) {
        return pushFile(token, "voice", file);
    }


    public static String pushVideo(String token, File file) {
        return pushFile(token, "video", file);
    }


    public static String pushFile(String token, File file) {
        return pushFile(token, "file", file);
    }

    public static void main(String[] args) {
        String token = getToken("wwc7a34f43098676ce", "3i_wI4bvKv9eyDIQpzaO-2Zs2JconZ7ejB_bV_v9VKg");
        System.out.println(token);
//        String res = pushImage(token,new File("/Users/liuhuiliang/img/splash2.jpeg"));
//        String id = null;
//        try {
//            id = new JSONObject(res).getString("media_id");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        System.out.println(res);
        sendTextMsgToUser(token, "CaiLiYing|FuHongZhen|LiuHuiLiang", "1000023", "dadaad");
//        sendImageMsgToUser(token, "CaiLiYing", "1000023", id);
    }
}
