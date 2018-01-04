package com.sgc.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.sgc.app.service.TimRest;
import com.sgc.comm.filter.Filter;
import com.sun.jmx.snmp.Timestamp;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class TimRestImpl implements TimRest {
    //app基本信息
    protected String sdkappid = "";
    protected String usersig = "";
    protected String identifier = "";

    //开放IM https接口参数, 一般不需要修改
    protected String http_type = "https://";
    protected String method = "post";
    protected String im_yun_url = "console.tim.qq.com";
    protected String version = "v4";
    protected String contenttype = "json";
    protected String apn = "0";


    @Value("{deps.path}")
    String DEPS_PATH;

    /**
     * 初始化函数
     *
     * @param sdkappid   应用的appid
     * @param identifier 访问接口的用户
     */
    public void init(String sdkappid, String identifier) {
        this.sdkappid = sdkappid;
        this.identifier = identifier;
    }

    /**
     * 构造访问REST服务器的参数,并访问REST接口
     *
     * @param server_name 服务名
     * @param cmd_name    命令名
     * @param identifier  用户名
     * @param usersig     用来鉴权的usersig
     * @param req_data    传递的json结构
     *                    $param bool $print_flag 是否打印请求，默认为打印
     * @return String out 返回的签名字符串
     */
    public String api(String server_name, String cmd_name, String identifier, String usersig, String req_data, boolean print_flag) throws IOException {
        //$req_tmp用来做格式化输出
        String req_tmp = JSON.parse(req_data).toString();

        //# 构建HTTP请求参数，具体格式请参考 REST API接口文档 (http://avc.qcloud.com/wiki/im/)(即时通信云-数据管理REST接口)
        String parameter = "usersig=" + this.usersig
                + "&identifier=" + this.identifier
                + "&sdkappid=" + this.sdkappid
                + "&contenttype=" + this.contenttype;
        String url = this.http_type + this.im_yun_url + "/" + this.version + "/" + server_name + "/" + cmd_name + "?" + parameter;
        //echo $url;
        if (print_flag) {
            System.out.println("Request Url:");
            System.out.println(url);

            System.out.println("Request Body:");
            //echo json_format($req_tmp);
            System.out.println(req_tmp);
        }
        String ret = null;
        try {
            ret = this.http_req("https", "post", url, req_data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //var_dump($ret);
        return ret;

    }

    /**
     * 构造访问REST服务器参数,并发访问REST服务器
     *
     * @param server_name 服务名
     * @param cmd_name    命令名
     * @param identifier  用户名
     * @param usersig     用来鉴权的usersig
     * @param req_data    传递的json结构
     *                    $param bool $print_flag 是否打印请求，默认为打印
     * @return String out 返回的签名字符串
     */
    public String multi_api(String server_name, String cmd_name, String identifier, String usersig, String req_data, boolean print_flag) {

        //$req_tmp用来做格式化控制台输出,同时作为多路访问需要的数组结构
        String req_tmp = JSON.parse(req_data).toString();
        //# 构建HTTP请求参数，具体格式请参考 REST API接口文档 (http://avc.qcloud.com/wiki/im/)(即时通信云-数据管理REST接口)
        String parameter = "usersig=" + this.usersig
                + "&identifier=" + this.identifier
                + "&sdkappid=" + this.sdkappid
                + "&contenttype=" + this.contenttype;

        String url = this.http_type + this.im_yun_url + "/" + this.version + "/" + server_name + "/" + cmd_name + "?" + parameter;
        //echo $url;
        if (print_flag) {
            System.out.println("Request Url:");
            System.out.println(url);

            System.out.println("Request Body:");
            System.out.println(req_tmp);
            System.out.println("");
        }
        String ret = this.http_req_multi("https", "post", url, req_tmp);
        //var_dump($ret);
        return ret;

    }

    public String getExecResult(Process process) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 独立模式根据Identifier生成UserSig的方法
     *
     * @param identifier         用户账号
     * @param expiry_after       过期时间
     * @param protected_key_path 私钥的存储路径及文件名
     * @return String out 返回的签名字符串
     */
    public String generate_user_sig(String identifier, String expiry_after, String protected_key_path, String tool_path) {
        // 创建临时sig文件
        String sig = this.mktemp();
        if (sig == null) {
            return null;
        }

        // 生成sig
        String cmd = DEPS_PATH + "/bin/tls_licence_tools"
                + " " + "gen"
                + " " + (protected_key_path)
                + " " + (sig)
                + " " + (this.sdkappid)
                + " " + (identifier);
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            try {
                int status = process.waitFor();
                if (status != 0) {
                    return null;
                } else {
                    // 读取sig
                    cmd = "cat " + " " + sig;
                    process = Runtime.getRuntime().exec(cmd);
                    int stat = process.waitFor();

                    if (stat != 0) {
                        return null;
                    } else {
                        return this.getExecResult(process);
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* 功能：删除临时sig文件
     * 说明：使用工具rm强制删除在deps/sig目录下指定的临时文件
     */
    private boolean rmtemp(String tmp) {
        String cmd = "rm -f " + tmp;
        //$ret = exec($cmd, $out, $status);
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            try {
                int status = process.waitFor();
                if (status != 0) {
                    return false;
                }
            } catch (InterruptedException e) {
                System.out.println(e.getStackTrace());
            }


            return true;
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
        return true;
    }

    /*
    /* 功能：创建临时sig文件
     * 说明：使用工具mktemp在deps/sig目录下创建临时文件sxb_sig.XXXXXXXXXX
     *        成功返回临时文件的绝对路径，失败返回null
     */
    private String mktemp() {
        String cmd = "mktemp -t -p " + DEPS_PATH + "/sig sxb_sig.XXXXXXXXXX";
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            try {
                int status = process.waitFor();
                if (status == 0) {
                    return DEPS_PATH + "/sig sxb_sig.XXXXXXXXXX";
                }
            } catch (InterruptedException e) {
                System.out.println(e.getStackTrace());
            }

        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
        return null;
    }

    /**
     * 托管模式设置用户凭证
     *
     * @param usr_sig protected_key_path 私钥的存储路径及文件名
     * @return bool 返回成功与否
     */
    public boolean set_user_sig(String usr_sig) {
        this.usersig = usr_sig;
        return true;
    }

    /**
     * 向Rest服务器发送请求
     *
     * @param http_type http类型,比如https
     * @param method    请求方式，比如POST
     * @param url       请求的url
     * @return String data 请求的数据
     */
    public String http_req1(String http_type, String method, String url, String data) {

        /*$ch = curl_init();
        if (strstr($http_type, "https"))
        {
            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
            curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
        }

        if ($method == "post")
        {
            curl_setopt($ch, CURLOPT_POST, 1);
            curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
        } else
        {
            $url = $url . "?" . $data;
        }
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_HEADER, 0);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_TIMEOUT,100000);//超时时间

        try
        {
            $ret=curl_exec($ch);
        }catch(Exception $e)
        {
            curl_close($ch);
            return json_encode(array("ret"",0,"msg"","failure"));
        }
        curl_close($ch);
        return $ret;*/
        return null;
    }

    public String http_req(String type, String method, String url, String data) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        StringEntity strEntity;
        strEntity = new StringEntity(data);
        strEntity.setContentEncoding("UTF8");
        strEntity.setContentType("application/json");
        HttpResponse response = null;
        if (method == "POST") {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(strEntity);
            response = client.execute(httpPost);
        } else {
            url += data;
            HttpGet httpGet = new HttpGet(url);
            response = client.execute(httpGet);
        }

        if (response.getStatusLine().getStatusCode() == 200) {
            Map<String, String> ret = new HashMap<>();
            ret.put("ret", "1");
            ret.put("msg", "request fail");
            JSONObject json = JSONObject.fromObject(ret);
            return json.toString();
        }
        return null;
    }

    /**
     * 向Rest服务器发送多个请求(并发)
     *
     * @param http_type http类型,比如https
     * @param method    请求方式，比如POST
     * @param url       请求的url
     * @return bool 是否成功
     */
    public String http_req_multi1(String http_type, String method, String url, String data) {
        /*
        $mh = curl_multi_init();
        $ch_list = array();
        $i = -1;
        $req_list = array();
        foreach($data as $req_data)
        {
            $i++;
            $req_data = json_encode(req_data, false);
            $ch = curl_init();
            if ($http_type == "https://")
            {
                curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
                curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 1);
            }

            if ($method == "post")
            {
                curl_setopt($ch, CURLOPT_POST, 1);
                curl_setopt($ch, CURLOPT_POSTFIELDS, req_data, false);
            } else
            {
                $url = $url . "?" . $data;
            }
            curl_setopt($ch, CURLOPT_URL, $url);
            curl_setopt($ch, CURLOPT_HEADER, 0);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
            curl_setopt($ch, CURLOPT_TIMEOUT,100000);//超时时间
            curl_multi_add_handle($mh, $ch);
            $ch_list[] = $ch;
            $req_list[] = $req_data;
        }
        try
        {
            do{
                $mret = curl_multi_exec($mh, $active);
            }while($mret == CURLM_CALL_MULTI_PERFORM);

            while($active and $mret == CURLM_OK){
            if(curl_multi_select($mh) === -1){
                usleep(100);
            }
            do{
                $mret = curl_multi_exec($mh, $active);
            }while($mret == CURLM_CALL_MULTI_PERFORM);
        }
        }catch(Exception $e)
        {
            curl_close($ch);
            return json_encode(array("ret"",0,"msg"","failure"));
        }
        for($i = 0; $i < count($ch_list); $i++)
        {
            $ret = curl_multi_getcontent($ch_list[$i]);
            if(strstr($ret, "URL_INFO"))
            {
                curl_multi_close($mh);
                return $ret;
            }
            $ret = json_decode($ret, true);
            echo json_format($ret);
        }
        curl_multi_close($mh);
        return true;
        */
        return null;
    }

    public String http_req_multi(String http_type, String method, String url, String data) {
        return "";
    }

    //#REST API 访问接口集合
    //#参数详情见RestInterface

    public List openim_send_msg(String account_id, String receiver, String text_content) {

        ////#构造高级接口所需参数
        List msg_content = new ArrayList();
        //创建array 所需元素
        Map<String, Object> msg_content_elem = new HashMap<>();
        msg_content_elem.put("MsgType", "TIMTextElem");
        Map<String, String> msg_content_elem_txt = new HashMap<>();
        msg_content_elem_txt.put("Text", text_content);
        msg_content_elem.put("MsgContent", msg_content_elem_txt);

        //将创建的元素$msg_content_elem, 加入array $msg_content
        msg_content.add(msg_content_elem);

        List msg2 = this.openim_send_msg2(account_id, receiver, msg_content);
        return msg2;
    }

    public Map openpic_pic_upload(String account_id, String receiver, String pic_path, int busi_type) {
        /*
        //#获取长度和md5值
        $pic_data = file_get_contents($pic_path);
        $md5 = md5($pic_data);
        $pic_size = filesize($pic_path);

		#进行base64处理
                $fp = fopen($pic_path, "r");
        $pic_data = fread($fp, $pic_size);

        $slice_data = array();
        $slice_size = array();
        $SLICE_SIZE = 32 * 4096;

        //对文件进行分片
        for ($i = 0; $i < $pic_size; $i = $i + $SLICE_SIZE) {
            if ($i + $SLICE_SIZE > $pic_size) {
                break;
            }
            $slice_tmp = substr($pic_data, $i, $SLICE_SIZE);
            $slice_tmp = chunk_split(base64_encode($slice_tmp));
            $slice_tmp = str_replace("\r\n", "", $slice_tmp);
            $slice_size[] =$SLICE_SIZE;
            $slice_data[] =$slice_tmp;
        }

        //最后一个分片
        if ($i - $SLICE_SIZE < $pic_size) {
            $slice_size[] =$pic_size - $i;
            $tmp = substr($pic_data, $i, $pic_size - $i);
            $slice_size[] =strlen($tmp);
            $tmp = chunk_split(base64_encode($tmp));
            $tmp = str_replace("\r\n", "", $tmp);

            $slice_data[] =$tmp;
        }

        $pic_rand = rand(1, 65535);
        $time_stamp = time();
        $req_data_list = array();
        $sentOut = 0;
        printf("handle %d segments\n", count($slice_data) - 1);
        for ($i = 0; $i < count($slice_data) - 1; $i++) {
            ////#构造消息
            $msg = array(
                    "From_Account", String account_id,  //发送者
                    "To_Account", $receiver,      //接收者
                    "App_Version", 1.4,       //应用版本号
                    "Seq", $i + 1,                      //同一个分片需要保持一致
                    "Timestamp", $time_stamp,         //同一张图片的不同分片需要保持一致
                    "Random", $pic_rand,              //同一张图片的不同分片需要保持一致
                    "File_Str_Md5", $md5,         //图片MD5，验证图片的完整性
                    "File_Size", $pic_size,       //图片原始大小
                    "Busi_Id", $busi_type,                    //群消息:1 c2c消息:2 个人头像：3 群头像：4
                    "PkgFlag", 1,                 //同一张图片要保持一致: 0表示图片数据没有被处理 ；1-表示图片经过base64编码，固定为1
                    "Slice_Offset", $i * $SLICE_SIZE,           //必须是4K的整数倍
                    "Slice_Size", $slice_size[$i],        //必须是4K的整数倍,除最后一个分片列外
                    "Slice_Data", $slice_data[$i]     //PkgFlag=1时，为base64编码
            );
            array_push($req_data_list, $msg);
            $sentOut = 0;
            if ($i != 0 && ($i + 1) % 4 == 0) {
                //将消息序列化为json串
                $req_data_list = json_encode($req_data_list);
                printf("\ni = %d, call multi_api once\n", $i);
                $ret = this.multi_api("openpic", "pic_up", this.identifier, this.usersig, $req_data_list, false);
                if (gettype($ret) == "string") {
                    $ret = json_decode($ret, true);
                    return $ret;
                }
                $req_data_list = array();
                $sentOut = 1;
            }
        }

        if ($sentOut == 0) {
            $req_data_list = json_encode($req_data_list);
            printf("\ni = %d, call multi_api once\n", $i);
            this.multi_api("openpic", "pic_up", this.identifier, this.usersig, $req_data_list, false);
        }

        //#最后一个分片

        $msg = array(
                "From_Account", String account_id,    //发送者
                "To_Account", $receiver,        //接收者
                "App_Version", 1.4,        //应用版本号
                "Seq", $i + 1,                        //同一个分片需要保持一致
                "Timestamp", $time_stamp,            //同一张图片的不同分片需要保持一致
                "Random", $pic_rand,                //同一张图片的不同分片需要保持一致
                "File_Str_Md5", $md5,            //图片MD5，验证图片的完整性
                "File_Size", $pic_size,        //图片原始大小
                "Busi_Id", $busi_type,                    //群消息:1 c2c消息:2 个人头像：3 群头像：4
                "PkgFlag", 1,                    //同一张图片要保持一致: 0表示图片数据没有被处理 ；1-表示图片经过base64编码，固定为1
                "Slice_Offset", $i * $SLICE_SIZE,            //必须是4K的整数倍
                "Slice_Size", $slice_size[count($slice_data) - 1],        //必须是4K的整数倍,除最后一个分片列外
                "Slice_Data", $slice_data[count($slice_data) - 1]        //PkgFlag=1时，为base64编码
        );

        $req_data = json_encode($msg);
        $ret = this.api("openpic", "pic_up", this.identifier, this.usersig, $req_data, false);
        $ret = json_decode($ret, true);
        echo json_format ($ret);
        return $ret;
        */
        return null;
    }

    public List openim_send_msg_pic(String account_id, String receiver, String pic_path) {
        /*
        //#构造高级接口所需参数
        //上传图片并获取url
        $busi_type = 2; //表示C2C消息
        $ret = this.openpic_pic_upload(account_id, receiver, pic_path, busi_type);
        $tmp = $ret["URL_INFO"];

        $uuid = $ret["File_UUID"];
        $pic_url = $tmp[0]["DownUrl"];

        $img_info = array();
        $img_tmp = $ret["URL_INFO"][0];
        if ($img_tmp["PIC_TYPE"] == 4) {
            $img_tmp["PIC_TYPE"] = 3;
        }
        $img_info_elem1 = array(
                "URL", $img_tmp["DownUrl"],
                "Height", $img_tmp["PIC_Height"],
                "Size", $img_tmp["PIC_Size"],
                "Type", $img_tmp["PIC_TYPE"],
                "Width", $img_tmp["PIC_Width"]
        );

        $img_tmp = $ret["URL_INFO"][1];
        if ($img_tmp["PIC_TYPE"] == 4) {
            $img_tmp["PIC_TYPE"] = 3;
        }
        $img_info_elem2 = array(
                "URL", $img_tmp["DownUrl"],
                "Height", $img_tmp["PIC_Height"],
                "Size", $img_tmp["PIC_Size"],
                "Type", $img_tmp["PIC_TYPE"],
                "Width", $img_tmp["PIC_Width"]
        );

        $img_tmp = $ret["URL_INFO"][2];
        if ($img_tmp["PIC_TYPE"] == 4) {
            $img_tmp["PIC_TYPE"] = 3;
        }
        $img_info_elem3 = array(
                "URL", $img_tmp["DownUrl"],
                "Height", $img_tmp["PIC_Height"],
                "Size", $img_tmp["PIC_Size"],
                "Type", $img_tmp["PIC_TYPE"],
                "Width", $img_tmp["PIC_Width"]
        );

        array_push($img_info, $img_info_elem1);
        array_push($img_info, $img_info_elem2);
        array_push($img_info, $img_info_elem3);
        $msg_content = array();
        //创建array 所需元素
        $msg_content_elem = array(
                "MsgType", "TIMImageElem",       //文本类型
                "MsgContent", array(
                        "UUID", $uuid,
                        "ImageInfoArray", $img_info,
                        )
        );
        //将创建的元素$msg_content_elem, 加入array $msg_content
        array_push($msg_content, $msg_content_elem);

        $ret = this.openim_send_msg2(String account_id, $receiver, $msg_content);
        return $ret;
        */
        return null;
    }

    public List openim_send_msg2(String account_id, String receiver, List msg_content) {
        List ret = new ArrayList();
        //#构造新消息
        Map<String, Object> msg = new HashMap<>();
        Random random = new Random();
        int rndSeq = random.nextInt(65535);
        int rndMsg = random.nextInt(65535);
        Timestamp time = new Timestamp(new Date().getTime());
        msg.put("To_Account", receiver);
        msg.put("MsgSeq", rndSeq);
        msg.put("MsgRandom", rndMsg);
        msg.put("MsgTimeStamp", time);
        msg.put("MsgBody", msg_content);
        msg.put("From_Account", account_id);
        //#将消息序列化为json串
        JSONObject json = JSONObject.fromObject(msg);
        String req_data = json.toString();

        try {
            String res = this.api("openim", "sendmsg", this.identifier, this.usersig, req_data,false);
            ret.add(res);
        }catch (IOException e){
            e.printStackTrace();
        }
        return ret;
    }

    public List openim_batch_sendmsg(List account_list, String text_content) {

        //#构造高级接口所需参数
        List msg_content = new ArrayList();
        //创建array 所需元素
        Map<String, Object> msg_content_elem = new HashMap<>();
        Map<String, Object> msg_content_elem_text = new HashMap<>();
        msg_content_elem_text.put("Text", text_content);

        msg_content_elem.put("MsgType", "TIMTextElem");
        msg_content_elem.put("MsgContent", msg_content_elem_text);


        //将创建的元素$msg_content_elem, 加入array $msg_content
        msg_content.add(msg_content_elem);

        List ret = this.openim_batch_sendmsg2(account_list, msg_content);
        return ret;
    }

    public List openim_batch_sendmsg_pic(String account_list, String pic_path) {
        /*
        ////#构造高级接口所需参数
        //上传图片并获取url
        $busi_type = 2; //表示C2C消息
        $ret = this.openpic_pic_upload(this.identifier, $account_list[0], $pic_path, $busi_type);
        $tmp = $ret["URL_INFO"];

        $uuid = $ret["File_UUID"];
        $pic_url = $tmp[0]["DownUrl"];

        $img_info = array();
        $img_tmp = $ret["URL_INFO"][0];
        if ($img_tmp["PIC_TYPE"] == 4) {
            $img_tmp["PIC_TYPE"] = 3;
        }
        $img_info_elem1 = array(
                "URL", $img_tmp["DownUrl"],
                "Height", $img_tmp["PIC_Height"],
                "Size", $img_tmp["PIC_Size"],
                "Type", $img_tmp["PIC_TYPE"],
                "Width", $img_tmp["PIC_Width"]
        );

        $img_tmp = $ret["URL_INFO"][1];
        if ($img_tmp["PIC_TYPE"] == 4) {
            $img_tmp["PIC_TYPE"] = 3;
        }
        $img_info_elem2 = array(
                "URL", $img_tmp["DownUrl"],
                "Height", $img_tmp["PIC_Height"],
                "Size", $img_tmp["PIC_Size"],
                "Type", $img_tmp["PIC_TYPE"],
                "Width", $img_tmp["PIC_Width"]
        );

        $img_tmp = $ret["URL_INFO"][2];
        if ($img_tmp["PIC_TYPE"] == 4) {
            $img_tmp["PIC_TYPE"] = 3;
        }
        $img_info_elem3 = array(
                "URL", $img_tmp["DownUrl"],
                "Height", $img_tmp["PIC_Height"],
                "Size", $img_tmp["PIC_Size"],
                "Type", $img_tmp["PIC_TYPE"],
                "Width", $img_tmp["PIC_Width"]
        );

        array_push($img_info, $img_info_elem1);
        array_push($img_info, $img_info_elem2);
        array_push($img_info, $img_info_elem3);
        $msg_content = array();
        //创建array 所需元素
        $msg_content_elem = array(
                "MsgType", "TIMImageElem",       //文本类型
                "MsgContent", array(
                        "UUID", $uuid,
                        "ImageInfoArray", $img_info,
                        )
        );
        //将创建的元素$msg_content_elem, 加入array $msg_content
        array_push($msg_content, $msg_content_elem);

        $ret = this.openim_batch_sendmsg2($account_list, $msg_content);
        return $ret;
        */
        return null;
    }

    public List openim_batch_sendmsg2(List account_list, List msg_content) {

        ////#构造新消息
        Map<String, Object> msg = new HashMap<>();
        Random random = new Random();
        int rand = random.nextInt(65535);
        msg.put("To_Account", account_list);
        msg.put("MsgRandom", rand);
        msg.put("MsgBody", msg_content);
        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        List ret = new ArrayList();
        try {
            String res = this.api("openim", "batchsendmsg", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public List account_import(String identifier, String nick, String face_url) {
        List ret = new ArrayList();
        ////#构造新消息
        Map<String, String> msg = new HashMap<>();
        msg.put("Identifier", identifier);
        msg.put("Nick", nick);
        msg.put("FaceUrl", face_url);
        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("im_open_login_svc", "account_import", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public List register_account(String identifier, String identifierType, String password) {
        List ret = new ArrayList();
        ////#构造新消息
        Map<String, String> msg = new HashMap<>();
        msg.put("Identifier", identifier);
        msg.put("IdentifierType", identifierType);
        msg.put("Password", password);
        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();

        try {
            String res = this.api("registration_service", "register_account_v1", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public List profile_portrait_get(String account_id) {
        //#构造高级接口所需参数
        List account_list = new ArrayList();

        account_list.add(account_id);

        List tag_list = new ArrayList();
        tag_list.add("Tag_Profile_IM_Nick");
        tag_list.add("Tag_Profile_IM_AllowType");


        List res = this.profile_portrait_get2(account_list, tag_list);

        return res;
    }

    public List profile_portrait_get2(List account_list, List tag_list) {
        List ret = new ArrayList();
        //#构造高级接口所需参数
        Map<String, Object> msg = new HashMap<>();
        msg.put("From_Account", this.identifier);
        msg.put("To_Account", account_list);
        msg.put("TagList", tag_list);
        msg.put("LastStandardSequence", 0);
        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        String res = null;
        try {
            res = this.api("profile", "portrait_get", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List profile_portrait_set(String account_id, String new_name) {

        //#构造高级接口所需参数
        List profile_list = new ArrayList();

        List profile_nick = new ArrayList();
        profile_nick.add("Tag");
        profile_nick.add("Tag_Profile_IM_Nick");
        profile_nick.add("Value");
        profile_nick.add(new_name);

        //加好友验证方式
        List profile_allow = new ArrayList();
        profile_allow.add("Tag");
        profile_allow.add("Tag_Profile_IM_AllowType");
        profile_allow.add("Value");
        profile_allow.add("NeedPermission");

        profile_list.add(profile_nick);


        List ret = this.profile_portrait_set2(account_id, profile_list);
        return ret;
    }

    public List profile_portrait_set2(String account_id, List profile_list) {
        List ret = new ArrayList();
        //#构造新消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("From_Account", account_id);
        msg.put("ProfileItem", profile_list);
        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();

        String res = null;
        try {
            res = this.api("profile", "portrait_set", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List sns_friend_import(String account_id, String receiver) {
        List ret = new ArrayList();
        //#构造新消息
        Map<String, Object> msg = new HashMap<>();
        List items = new ArrayList();
        msg.put("From_Account", account_id);
        msg.put("AddFriendItem", items);

        Map<String, Object> receiver_arr = new HashMap<>();
        receiver_arr.put("To_Account", receiver);
        receiver_arr.put("Remark", "");
        receiver_arr.put("AddSource", "AddSource_Type_Unknow");
        receiver_arr.put("AddWording", "");

        msg.put("AddFriendItem", receiver_arr);

        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("sns", "friend_import", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }


    public List sns_friend_delete(String account_id, String frd_id) {
        List ret = new ArrayList();
        ////#构造新消息
        List frd_list = new ArrayList();
        Map<String, Object> msg = new HashMap<>();
        //要添加的好友用户
        frd_list.add(frd_id);

        msg.put("From_Account", account_id);
        msg.put("To_Account", frd_list);
        msg.put("DeleteType", "Delete_Type_Both");
        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("sns", "friend_delete", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List sns_friend_delete_all(String account_id) {
        List ret = new ArrayList();
        ////#构造新消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("From_Account", account_id);
        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("sns", "friend_delete_all", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List sns_friend_check(String account_id, String to_account) {
        List ret = new ArrayList();
        ////#构造高级接口所需参数
        List to_account_list = new ArrayList();
        //要添加的好友用户
        to_account_list.add(to_account);
        Map<String, Object> msg = new HashMap<>();

        msg.put("From_Account", account_id);
        msg.put("To_Account", to_account_list);

        ret = this.sns_friend_check2(account_id, to_account_list, "CheckResult_Type_Both");
        return ret;
    }

    public List sns_friend_check2(String account_id, List to_account_list, String check_type) {
        List ret = new ArrayList();
        ////#构造新消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("From_Account", account_id);
        msg.put("To_Account", to_account_list);
        msg.put("CheckType", check_type);
        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("sns", "friend_check", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List sns_friend_get_all(String account_id) {

        ////#构造高级接口所需参数
        List tag_list = new ArrayList();
        tag_list.add("Tag_Profile_IM_Nick");
        tag_list.add("Tag_SNS_IM_Remark");

        List ret = this.sns_friend_get_all2(account_id, tag_list);
        return ret;
    }

    public List sns_friend_get_all2(String account_id, List tag_list) {
        List ret = new ArrayList();
        ////#构造新消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("From_Account", account_id);
        msg.put("TimeStamp", 0);
        msg.put("TagList", tag_list);
        msg.put("LastStandardSequence", 1);
        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("sns", "friend_get_all", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List sns_friend_get_list(String account_id, String frd_id) {

        ////#构造高级接口所需参数
        List frd_list = new ArrayList();
        frd_list.add(frd_id);

        List tag_list = new ArrayList();
        tag_list.add("Tag_Profile_IM_Nick");
        tag_list.add("Tag_SNS_IM_Remark");

        List ret = this.sns_friend_get_list2(account_id, frd_list, tag_list);
        return ret;
    }

    public List sns_friend_get_list2(String account_id, List frd_list, List tag_list) {
        List ret = new ArrayList();
        ////#构造新消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("From_Account", account_id);
        msg.put("To_Account", frd_list);
        msg.put("TagList", tag_list);
        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("sns", "friend_get_list", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List group_get_appid_group_list() {

        //#构造高级接口所需参数
        List ret = this.group_get_appid_group_list2(50, 0, null);
        return ret;
    }


    public List group_get_appid_group_list2(int limit, int offset, String group_type) {
        List ret = new ArrayList();
        //#构造新消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("Limit", limit);
        msg.put("Offset", offset);
        msg.put("GroupType", group_type);

        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("group_open_http_svc", "get_appid_group_list", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List group_create_group(String group_type, String group_name, String owner_id) {

        //#构造高级接口所需参数
        Map<String, Object> info_set = new HashMap<>();
        info_set.put("group_id", null);
        info_set.put("introduction", null);
        info_set.put("notification", null);
        info_set.put("face_url", null);
        info_set.put("max_member_num", 500);

        List mem_list = new ArrayList();

        List ret = this.group_create_group2(group_type, group_name, owner_id, info_set, mem_list);
        return ret;
    }

    public List group_create_group2(String group_type, String group_name, String owner_id, Map info_set, List mem_list) {
        List ret = new ArrayList();
        //#构造新消息
        Map<String, Object> msg = new HashMap<>();

        msg.put("Type", group_type);
        msg.put("Name", group_name);
        msg.put("Owner_Account", owner_id);
        msg.put("GroupId", info_set.get("group_id"));
        msg.put("Introduction", info_set.get("introduction"));
        msg.put("Notification", info_set.get("notification"));
        msg.put("FaceUrl", info_set.get("face_url"));
        msg.put("MaxMemberCount", info_set.get("max_member_num"));
        //	"ApplyJoinOption", $info_set["apply_join"],
        msg.put("MemberList", mem_list);

        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("group_open_http_svc", "create_group", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List group_change_group_owner(String group_id, String new_owner) {
        List ret = new ArrayList();
        //#构造新消息
        Map<String, Object> msg = new HashMap<>();

        msg.put("GroupId", group_id);
        msg.put("NewOwner_Account", new_owner);

        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);

        String req_data = jsonObject.toString();
        try {
            String res = this.api("group_open_http_svc", "change_group_owner", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List group_get_group_info(String group_id) {

        //#构造高级接口所需参数
        List group_list = new ArrayList();
        group_list.add(group_id);

        List base_info_filter = Arrays.asList(
                "Type",               //群类型(包括Public(公开群), Private(私密群), ChatRoom(聊天室))
                "Name",               //群名称
                "Introduction",       //群简介
                "Notification",       //群公告
                "FaceUrl",            //群头像url地址
                "CreateTime",         //群组创建时间
                "Owner_Account",      //群主id
                "LastInfoTime",       //最后一次系统通知时间
                "LastMsgTime",        //最后一次消息发送时间
                "MemberNum",          //群组当前成员数目
                "MaxMemberNum",       //群组内最大成员数目
                "ApplyJoinOption"     //加群处理方式(比如FreeAccess 自由加入)
        );
        List member_info_filter = Arrays.asList(
                "Account",         // 成员ID
                "Role",            // 成员身份
                "JoinTime",        // 成员加入时间
                "LastSendMsgTime", // 该成员最后一次发送消息时间
                "ShutUpUntil"      // 该成员被禁言直到某时间
        );

        List app_define_filter = Arrays.asList(
                "GroupTestData1"  //自定义数据
        );

        List ret = this.group_get_group_info2(group_list, base_info_filter, member_info_filter, app_define_filter);
        return ret;
    }

    public List group_get_group_info2(List group_list, List base_info_filter, List member_info_filter, List app_define_filter) {
        List ret = new ArrayList();
        //#构造新消息
        Filter filter = new Filter();
        filter.setGroupBaseInfoFilter(base_info_filter);
        filter.setMemberInfoFilter(member_info_filter);
        filter.setAppDefinedDataFilter_Group(app_define_filter);
        Map<String, Object> msg = new HashMap<>();

        msg.put("GroupIdList", group_list);
        msg.put("ResponseFilter", filter);
        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("group_open_http_svc", "get_group_info", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return ret;
    }

    public List group_get_group_member_info(String group_id, int limit, int offset) {
        List ret = new ArrayList();
        //#构造新消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("GroupId", group_id);
        msg.put("Limit", limit);
        msg.put("Offset", offset);

        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("group_open_http_svc", "get_group_member_info", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List group_modify_group_base_info(String group_id, String group_name) {

        //#构造高级接口所需参数
        Map<String, Object> info_set = new HashMap<>();

        info_set.put("introduction", null);
        info_set.put("notification", null);
        info_set.put("face_url", null);
        info_set.put("max_member_num", null);
        //	"apply_join", "NeedPermission"
        List app_define_list = new ArrayList();

        List ret = this.group_modify_group_base_info2(group_id, group_name, info_set, app_define_list);
        return ret;
    }

    public List group_modify_group_base_info2(String group_id, String group_name, Map info_set, List app_define_list) {
        List ret = new ArrayList();
        //#构造新消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("GroupId", group_id);
        msg.put("Name", group_name);
        msg.put("Introduction", info_set.get("introduction"));
        msg.put("Notification", info_set.get("notification"));
        msg.put("FaceUrl", info_set.get("face_url"));
        msg.put("MaxMemberNum", info_set.get("max_member_num"));
        //	"ApplyJoinOption", info_set.get("apply_join"));
        msg.put("AppDefinedData", app_define_list);

        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("group_open_http_svc", "modify_group_base_info", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;

    }

    public List group_add_group_member(String group_id, String member_id, int silence) {
        List ret = new ArrayList();
        //#构造新消息
        List mem_list = new ArrayList();
        Map<String, Object> mem_elem = new HashMap<>();
        mem_elem.put("Member_Account", member_id);
        mem_list.add(mem_elem);
        Map<String, Object> msg = new HashMap<>();
        msg.put("GroupId", group_id);
        msg.put("MemberList", mem_list);
        msg.put("Silence", silence);
        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("group_open_http_svc", "add_group_member", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List group_delete_group_member(String group_id, String member_id, int silence) {
        List ret = new ArrayList();
        //#构造新消息
        List mem_list = new ArrayList();
        mem_list.add(member_id);
        Map<String, Object> msg = new HashMap<>();

        msg.put("GroupId", group_id);
        msg.put("MemberToDel_Account", mem_list);
        msg.put("Silence", silence);

        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("group_open_http_svc", "delete_group_member", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List group_modify_group_member_info(String group_id, String account_id, String role) {

        //#构造高级接口所需参数
        List ret = this.group_modify_group_member_info2(group_id, account_id, role, "AcceptAndNotify", 0);
        return ret;
    }

    public List group_modify_group_member_info2(String group_id, String account_id, String role, String msg_flag, int shutup_time) {
        List ret = new ArrayList();
        //#构造新消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("GroupId", group_id);
        msg.put("Member_Account", account_id);
        msg.put("Role", role);

        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("group_open_http_svc", "modify_group_member_info", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List group_destroy_group(String group_id) {
        List ret = new ArrayList();
        //#构造新消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("GroupId", group_id);
        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();

        try {
            String res = this.api("group_open_http_svc", "destroy_group", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List group_get_joined_group_list(String account_id) {

        //#构造高级接口所需参数
        List base_info_filter = Arrays.asList(
                "Type",               //群类型(包括Public(公开群), Private(私密群), ChatRoom(聊天室))
                "Name",               //群名称
                "Introduction",       //群简介
                "Notification",       //群公告
                "FaceUrl",            //群头像url地址
                "CreateTime",         //群组创建时间
                "Owner_Account",      //群主id
                "LastInfoTime",       //最后一次系统通知时间
                "LastMsgTime",        //最后一次消息发送时间
                "MemberNum",          //群组当前成员数目
                "MaxMemberNum",       //群组内最大成员数目
                "ApplyJoinOption"     //申请加群处理方式(比如FreeAccess 自由加入, NeedPermission 需要同意)
        );


        List self_info_filter = Arrays.asList(
                "Role",            //群内身份(Amin/Member)
                "JoinTime",        //入群时间
                "MsgFlag",         //消息屏蔽类型
                "UnreadMsgNum"     //未读消息数量
        );

        List ret = this.group_get_joined_group_list2(account_id, null, base_info_filter, self_info_filter);
        return ret;
    }

    public List group_get_joined_group_list2(String account_id, String group_type, List base_info_filter, List self_info_filter) {
        List ret = new ArrayList();
        //#构造新消息
        Filter filter = new Filter();
        filter.setGroupBaseInfoFilter(base_info_filter);
        filter.setSelfInfoFilter(self_info_filter);
        Map<String, Object> msg = new HashMap<>();
        msg.put("Member_Account", account_id);
        msg.put("ResponseFilter", filter);

        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);

        String req_data = jsonObject.toString();
        try {
            String res = this.api("group_open_http_svc", "get_joined_group_list", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List group_get_role_in_group(String group_id, String member_id) {
        List ret = new ArrayList();
        //#构造新消息
        List mem_list = new ArrayList();
        mem_list.add(member_id);

        Map<String, Object> msg = new HashMap<>();
        msg.put("GroupId", group_id);
        msg.put("User_Account", mem_list);
        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("group_open_http_svc", "get_role_in_group", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List group_forbid_send_msg(String group_id, String member_id, int second) {
        List ret = new ArrayList();
        //#构造新消息
        List mem_list = new ArrayList();
        mem_list.add(member_id);
        Map<String, Object> msg = new HashMap<>();
        msg.put("GroupId", group_id);
        msg.put("Members_Account", mem_list);
        msg.put("ShutUpTime", second);

        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();

        try {
            String res = this.api("group_open_http_svc", "forbid_send_msg", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List group_send_group_msg(String account_id, String group_id, String text_content) {
        //#构造高级接口所需参数
        List msg_content = new ArrayList();
        //创建array 所需元素
        Map<String, Object> msg_content_elem = new HashMap<>();
        Map<String, Object> txt = new HashMap<>();
        txt.put("Text", text_content);

        msg_content_elem.put("MsgType", "TIMTextElem");       //文本类型
        msg_content_elem.put("MsgContent", txt);

        msg_content.add(msg_content_elem);
        List ret = this.group_send_group_msg2(account_id, group_id, msg_content);
        return ret;
    }

    public List group_send_group_msg_pic(String account_id, String group_id, String pic_path) {
        /*
        //#构造高级接口所需参数
        //上传图片并获取url
        int busi_type = 1; //表示群消息
        Map ret = this.openpic_pic_upload(account_id, group_id, pic_path, busi_type);
        List tmp = (List) ret.get("URL_INFO");

        String uuid = ret.get("File_UUID").toString();
        Map tmpMap = (Map) tmp.get(0);
        String pic_url = tmpMap.get("Downurl").toString();

        List img_info = new ArrayList();


        if (Integer.valueOf(tmpMap.get("PIC_TYPE").toString()) == 4) {
            tmpMap.put("PIC_TYPE", 3);
        }
        Map<String, Object> img_info_elem1 = new HashMap();
        "URL", $img_tmp["DownUrl"],
                "Height", $img_tmp["PIC_Height"],
                "Size", $img_tmp["PIC_Size"],
                "Type", $img_tmp["PIC_TYPE"],
                "Width", $img_tmp["PIC_Width"]
        );

        $img_tmp = $ret["URL_INFO"][1];
        if ($img_tmp["PIC_TYPE"] == 4) {
            $img_tmp["PIC_TYPE"] = 3;
        }
        $img_info_elem2 = array(
                "URL", $img_tmp["DownUrl"],
                "Height", $img_tmp["PIC_Height"],
                "Size", $img_tmp["PIC_Size"],
                "Type", $img_tmp["PIC_TYPE"],
                "Width", $img_tmp["PIC_Width"]
        );

        $img_tmp = $ret["URL_INFO"][2];
        if ($img_tmp["PIC_TYPE"] == 4) {
            $img_tmp["PIC_TYPE"] = 3;
        }
        $img_info_elem3 = array(
                "URL", $img_tmp["DownUrl"],
                "Height", $img_tmp["PIC_Height"],
                "Size", $img_tmp["PIC_Size"],
                "Type", $img_tmp["PIC_TYPE"],
                "Width", $img_tmp["PIC_Width"]
        );

        array_push($img_info, $img_info_elem1);
        array_push($img_info, $img_info_elem2);
        array_push($img_info, $img_info_elem3);
        $msg_content = array();
        //创建array 所需元素
        $msg_content_elem = array(
                "MsgType", "TIMImageElem",       //文本类型
                "MsgContent", array(
                        "UUID", $uuid,
                        "ImageInfoArray", $img_info,
                        )
        );
        //将创建的元素$msg_content_elem, 加入array $msg_content
        array_push($msg_content, $msg_content_elem);

        $ret = this.group_send_group_msg2(account_id, group_id, msg_content);
        return $ret;
        */
        return null;
    }

    public List group_send_group_msg2(String account_id, String group_id, List msg_content) {
        List ret = new ArrayList();
        Random random = new Random();
        Integer rand = random.nextInt(65535);
        //#构造新消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("GroupId", account_id);
        msg.put("From_Account", account_id);
        msg.put("Random", rand);
        msg.put("MsgBody", msg_content);

        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();

        try {
            String res = this.api("group_open_http_svc", "send_group_msg", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public List group_send_group_system_notification(String group_id, String text_content, String receiver_id) {

        //#构造高级接口所需参数
        List receiver_list = new ArrayList();
        if (receiver_id != null) {
            receiver_list.add(receiver_id);
        }
        List ret = this.group_send_group_system_notification2(group_id, text_content, receiver_list);
        return ret;
    }

    public List group_send_group_system_notification2(String group_id, String content, List receiver_list) {
        List ret = new ArrayList();
        //#构造新消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("GroupId", group_id);
        msg.put("ToMembers_Account", receiver_list);
        msg.put("Content", content);

        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("group_open_http_svc", "send_group_system_notification", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List comm_rest(String server, String command, String req_body) {
        List ret = new ArrayList();
        //#将消息序列化为json串
        String req_data = req_body.toString();
        try {
            String res = this.api(server, command, this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List group_import_group_member(String group_id, String member_id, String role) {

        //#构造高级接口所需参数

        List member_list = new ArrayList();
        Map<String, Object> member_elem = new HashMap<>();
        member_elem.put("Member_Account", member_id);
        member_elem.put("Role", role);

        member_list.add(member_elem);
        List ret = this.group_import_group_member2(group_id, member_list);
        return ret;
    }

    public List group_import_group_member2(String group_id, List member_list) {
        List ret = new ArrayList();
        //#构造新消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("GroupId", group_id);
        msg.put("MemberList", member_list);
        //#将消息序列化为json串
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String req_data = jsonObject.toString();
        try {
            String res = this.api("group_open_http_svc", "import_group_member", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List group_import_group_msg(String group_id, String from_account, String text) {
        //#构造高级接口所需参数

        //构造MsgBody
        Map<String, Object> msg_content = new HashMap<>();
        msg_content.put("Text", text);

        List msg_body_elem = new ArrayList();
        msg_body_elem.add("MsgType");
        msg_body_elem.add("TIMTextElem");
        msg_body_elem.add("MsgContent");
        msg_body_elem.add(msg_content);


        List msg_body_list = new ArrayList();
        msg_body_list.add(msg_body_elem);

        Random random = new Random();
        Integer rand = random.nextInt(65535);
        //构造MsgList的一个元素
        Map<String, Object> msg_list_elem = new HashMap<>();
        msg_list_elem.put("From_Account", from_account);
        msg_list_elem.put("SendTime", new Timestamp(new Date().getTime()));
        msg_list_elem.put("Random", rand);
        msg_list_elem.put("MsgBody", msg_body_list);

        //构造MsgList
        List msg_list = new ArrayList();
        msg_list.add(msg_body_elem);

        List ret = this.group_import_group_msg2(group_id, msg_list);
        return ret;
    }

    public List group_import_group_msg2(String group_id, List msg_list) {

        //#构造新消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("GroupId", group_id);
        msg.put("MsgList", msg_list);
        //var_dump($msg);
        //#将消息序列化为json串
        JSONObject json = JSONObject.fromObject(msg);
        String req_data = json.toString();

        List ret = null;
        String res = null;
        try {
            res = this.api("group_open_http_svc", "import_group_msg", this.identifier, this.usersig, req_data, false);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return ret;
    }

    public List group_set_unread_msg_num(String group_id, String member_account, int unread_msg_num) {

        //#构造新消息
        Map<String, Object> msg = new HashMap<>();
        msg.put("GroupId", group_id);
        msg.put("Member_Account", member_account);
        msg.put("UnreadMsgNum", unread_msg_num);


        //#将消息序列化为json串
        JSONObject json = JSONObject.fromObject(msg);
        String req_data = json.toString();
        List ret = null;
        String res = null;
        try {
            res = this.api("group_open_http_svc", "set_unread_msg_num", this.identifier, this.usersig, req_data, false);
            ret.add(res);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }


/**
 * Json数据格式化方法
 *
 * @param array $data 数组数据
 * @param String indent 缩进字符，默认4个空格
 * @return sting json格式字符串
 */
        /*json_format($data,$indent=null)
                {

                // 对数组中每个元素递归进行urlencode操作，保护中文字符
                array_walk_recursive($data,"json_format_protect");

                // json encode
                $data=json_encode($data);

                // 将urlencode的内容进行urldecode
                $data=urldecode($data);

                // 缩进处理
                $ret="";
                $pos=0;
                $length=strlen($data);
                $indent=isset($indent)?$indent:"    ";
                $newline="\n";
                $prevchar="";
                $outofquotes=true;
                for($i=0;$i<=$length;$i++){
                $char=substr($data,$i,1);
                if($char==""" && $prevchar!="\\")
                {
                $outofquotes=!$outofquotes;
                }elseif(($char=="}"||$char=="]")&&$outofquotes)
                {
                $ret.=$newline;
                $pos--;
                for($j=0;$j<$pos; $j++){
        $ret.=$indent;
        }
        }
        $ret.=$char;
        if(($char==","||$char=="{"||$char=="[")&&$outofquotes)
        {
        $ret.=$newline;
        if($char=="{"||$char=="["){
        $pos++;
        }

        for($j=0;$j<$pos; $j++){
        $ret.=$indent;
        }
        }
        $prevchar=$char;
        }
        return $ret;
        }*/

    /**
     * json_formart辅助函数
     * @param String val 数组元素
     */
        /*json_format_protect(&$val)
        {
        if($val!==true&&$val!==false&&$val!==null)
        {
        $val=urlencode($val);
        }
        }*/

    /**
     * 判断操作系统位数
     * 64位返回true
     */
    public boolean is_64bit() {
        String arch = System.getProperty("os.arch");
        if(arch == "x64"){
            return true;
        }
        return false;
    }
}

