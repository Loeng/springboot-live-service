package com.sgc.app.service;

import java.util.List;
import java.util.Map;

public interface TimRest {
    /**
     * 初始化函数
     *
     * @param sdkappid   应用的appid
     * @param identifier 访问接口的用户
     * @return bool 返回true/false
     */
    public void init(String sdkappid, String identifier);

    /**
     * 独立模式根据Identifier生成UserSig的方法
     *
     * @param identifier         用户账号
     * @param expiry_after       过期时间
     * @param protected_key_path 私钥的存储路径及文件名
     * @return String out 返回的签名字符串
     */
    public String generate_user_sig(String identifier, String expiry_after, String protected_key_path, String tool_path);

    /**
     * 托管模式设置用户凭证
     *
     * @param usr_sig 私钥的存储路径及文件名
     * @return bool 返回成功与否
     */
    public boolean set_user_sig(String usr_sig);

    //################################ 单发消息 ###################################

    /**
     * 发文本消息
     *
     * @param account_id   发送者id
     * @param receiver     接收方的用户账号
     * @param text_content 消息内容(这里为文本消息)
     * @return array 通过解析REST接口json返回包得到的关联数组, 其中包含成功与否、及错误提示(如果有错误)等字段
     */
    public List openim_send_msg(String account_id, String receiver, String text_content);

    /**
     * 发图片消息(图片不大于10M)
     *
     * @param account_id 发送者id
     * @param receiver   接收方的用户账号
     * @param pic_path   要发送的图片本地路径
     * @return array 通过解析REST接口json返回包得到的关联数组, 其中包含成功与否、及错误提示(如果有错误)等字段
     */
    public List openim_send_msg_pic(String account_id, String receiver, String pic_path);

    /**
     * 单发消息(高级接口)
     *
     * @param account_id  发送者id
     * @param receiver    接收方的用户账号
     * @param msg_content 消息内容, php构造示例:
     *                    <p>
     *                    $msg_content = array();
     *                    //创建array 所需元素
     *                    $msg_content_elem = array(
     *                    'MsgType' => 'TIMTextElem',       //文本类型
     *                    'MsgContent' => array(
     *                    'Text' => "hello",                //hello 为文本信息
     *                    )
     *                    );
     *                    //将创建的元素$msg_content_elem, 加入array $msg_content
     *                    array_push($msg_content, $msg_content_elem);
     * @return array 通过解析REST接口json返回包得到的关联数组, 其中包含成功与否、及错误提示(如果有错误)等字段
     */
    public List openim_send_msg2(String account_id, String receiver, List msg_content);

    /**
     * 批量发文本消息
     *
     * @param account_list 接收消息的用户id集合
     * @param text_content 消息内容(这里为文本消息)
     * @return array 通过解析REST接口json返回包得到的关联数组, 其中包含成功与否、及错误提示(如果有错误)等字段
     */
    public List openim_batch_sendmsg(List account_list, String text_content);

    /**
     * 批量发图片
     *
     * @param account_list 接收消息的用户id集合
     * @param pic_path     要发送图片的本地路径
     * @return array 通过解析REST接口json返回包得到的关联数组, 其中包含成功与否、及错误提示(如果有错误)等字段
     */
    public List openim_batch_sendmsg_pic(String account_list, String pic_path);

    /**
     * 批量发消息(高级接口)
     *
     * @param account_list 接收消息的用户id集合
     * @param msg_content  消息内容, php构造示例:
     *                     <p>
     *                     $msg_content = array();
     *                     //创建array 所需元素
     *                     $msg_content_elem = array(
     *                     'MsgType' => 'TIMTextElem',       //文本??型
     *                     'MsgContent' => array(
     *                     'Text' => "hello",                //hello 为文本信息
     *                     )
     *                     );
     *                     //将创建的元素String msg_content_elem, 加入array $msg_content
     *                     array_push($msg_content, $msg_content_elem);
     * @return array 通过解析REST接口json返回包得到的关联数组, 其中包含成功与否、及错误提示(如果有错误)等字段
     */
    public List openim_batch_sendmsg2(List account_list, List msg_content);

    //################################ 帐号管理 ###################################

    /**
     * 独立模式帐号同步接口
     *
     * @param identifier 用户名
     * @param nick       用户昵称
     * @param face_url   用户头像URL
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List account_import(String identifier, String nick, String face_url);

    /**
     * 独立模式帐号同步接口
     *
     * @param identifier     为用户申请同步的帐号，长度为4-24个字符
     * @param identifierType Identifier的类型，1:手机号(国家码-手机号) 2:邮箱 3:字符串帐号
     * @param password       Identifier的密码，长度为8-16个字符
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List register_account(String identifier, String identifierType, String password);

    //################################ 资料管理 ###################################

    /**
     * 获取用户资料
     *
     * @param account_id 获取哪个用户的资料
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、拉取到的用户信息(如果成功)，及错误提示等字段
     */
    public List profile_portrait_get(String account_id);

    /**
     * 获取用户资料(高级接口)
     *
     * @param account_list 需要获取资料的帐号id集合, php构造示例:
     *                     <p>
     *                     $account_list = array();
     *                     array_push($account_list, $account_id);  //$account_id为用户id，需要用户传递
     * @param tag_list     需要拉取的字段,目前可拉取的字段:
     *                     1.昵称:"Tag_Profile_IM_Nick
     *                     2.加好友设置"Tag_Profile_IM_AllowType", php构造示例:
     *                     <p>
     *                     $tag_list = array(
     *                     "Tag_Profile_IM_Nick",                  //昵称
     *                     );
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、拉取到的用户信息(如果成功)，及错误提示等字段
     */
    public List profile_portrait_get2(List account_list, List tag_list);

    /**
     * 设置用户名称
     *
     * @param account_id 需要设置的用户
     * @param new_name   要设置为的用户名
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List profile_portrait_set(String account_id, String new_name);

    /**
     * 设置用户资料(高级接口)
     *
     * @param account_id   需要设置的用户
     * @param profile_list 设置选项集合，用户账号设置内容选项, 比如昵称, php构造示例:
     *                     <p>
     *                     //创建array $profile_list
     *                     $profile_list = array();
     *                     //创建昵称选项
     *                     $profile_nick = array(
     *                     "Tag" => "Tag_Profile_IM_Nick",             //用户昵称
     *                     "Value" => "new_name"                        //"new_name"要设置成的用户名
     *                     );
     *                     //加好友验证方式
     *                     $profile_allow = array(
     *                     "Tag" => "Tag_Profile_IM_AllowType",
     *                     "Value" => "AllowType_Type_NeedConfirm"
     *                     );
     *                     array_push($profile_list, $profile_nick);
     *                     array_push($profile_list, $profile_allow);
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */

    public List profile_portrait_set2(String account_id, List profile_list);


    //################################ 关系链管理 ###################################
    //      ## 关系链托管方式请详见:关系链系统概述 (http://avc.qcloud.com/wiki2.0/im/) 即时通信云-概述-关系链系统 ##

    /**
     * 建立双方好友关系
     *
     * @param account_id 发起者id
     * @param receiver   添加的用户，完成之后两者互为好友
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List sns_friend_import(String account_id, String receiver);

    /**
     * 解除双方好友关系
     *
     * @param account_id 用户id,即需要删除好友的用户
     * @param frd_id     需要删除的好友
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List sns_friend_delete(String account_id, String frd_id);

    /**
     * 解除所有好友关系
     *
     * @param account_id 用户id,即需要解除所有好友关系的用户
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List sns_friend_delete_all(String account_id);

    /**
     * 校验好友关系(默认双向严重)
     *
     * @param account_id 需要校验好友的用户id
     * @param to_account 校验是否为好友的id
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示、验证结果等字段
     */
    public List sns_friend_check(String account_id, String to_account);

    /**
     * 校验好友关系
     *
     * @param account_id      需要校验好友的用户id
     * @param to_account_list 校验是否为好友的id集合
     * @param check_type      校验类型，目前支持：单向校验"CheckResult_Type_Singal"，双向校验"CheckResult_Type_Both"
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示、验证结果等字段
     */
    public List sns_friend_check2(String account_id, List to_account_list, String check_type);

    /**
     * 拉取好友
     *
     * @param account_id 需要获取好友的用户id
     * @return array 通过解析REST接口json返回包得到的关联数组，包含拉取到的好友信息、成功与否及错误提示等字段
     */
    public List sns_friend_get_all(String account_id);

    /**
     * 拉取好友(高级接口)
     *
     * @param account_id 需要获取好友的用户id
     * @param tag_list   需要拉取的字段，该拉取协议是一条整合数据的协议，可以指定拉取自己好友的昵称
     *                   加好友设置以及对用户的备注等字段，如果需要拉取昵称字段，则这里就需要在Json数组中填入Tag_Profile_IM_Nick.
     *                   php构造示例:
     *                   <p>
     *                   $tag_list = array(
     *                   "Tag_Profile_IM_Nick",      //昵称选项
     *                   "Tag_SNS_IM_Remark"         //备注选项
     *                   );
     * @return array 通过解析REST接口json返回包得到的关联数组，包含拉取到的好友信息、成功与否及错误提示等字段
     */
    public List sns_friend_get_all2(String account_id, List tag_list);

    /**
     * 拉取指定好友的信息
     *
     * @param account_id 需要拉取好友的帐号
     * @param frd_id     需要被拉取的好友
     * @return array 通过解析REST接口json返回包得到的关联数组，包含拉取到的好友信息、成功与否及错误提示等字段
     */
    public List sns_friend_get_list(String account_id, String frd_id);

    /**
     * 拉取特定好友(高级接口)
     *
     * @param account_id 需要拉取好友的帐号
     * @param frd_list   拉取好友对象, php构造示例:
     *                   <p>
     *                   $frd_list = array();
     *                   array_push($frd_list, "leckie");  //"leckie" 为需要被拉取的好友id
     * @param tag_list   需要拉取属性的选项字段, 该拉取协议是一条整合数据的协议，可以指定拉取自己好友的昵称
     *                   、加好友设置以及对用户的备注等字段，如果需要拉取昵称字段，则这里就需要在Json数组中填入Tag_Profile_IM_Nick
     *                   <p>
     *                   $tag_list = array(
     *                   "Tag_Profile_IM_Nick",   //昵称选项
     *                   "Tag_SNS_IM_Remark"      //备注选项
     *                   );
     * @return array 通过解析REST接口json返回包得到的关联数组，包含拉取到的好友信息、成功与否及错误提示等字段
     */
    public List sns_friend_get_list2(String account_id, List frd_list, List tag_list);

    //################################ 群组管理 ###################################

    /**
     * 获取app中所有群组, 如果APP中的总群数量超过10000个，最多只会返回10000个(如果需要获取完整必须使用高级接口)
     *
     * @return array 通过解析REST接口json返回包得到的关联数组，包含拉取到的群组信息、成功与否及错误提示等字段
     */
    public List group_get_appid_group_list();

    /**
     * 获取app中所有群组(高级接口)
     *
     * @param limit      最多获取多少个群，不得超过10000, 如果不填，获取能获取的最大数量的群.
     * @param offset     控制从整个群组列表中的第多少个开始读取(从0开始). 对于分页请求（页码数字从1开始），每
     *                   一页的Offset值应当为：（页码数-1）×每页展示的群组数量, 如果不填从0开始.
     * @param group_type 如果仅需要返回特定群组形态的群组，可以通过GroupType进行过滤，但此时返回的TotalCount
     *                   的含义就变成了APP中该群组形态的群组总数. 例如：假设APP旗下总共50000个群组，其中有20000个为公开群组，如
     *                   果将请求包体中的GroupType设置为Public，那么不论limit和offset怎样设置，应答包体中的TotalCount都为20000，
     *                   且GroupIdList中的群组全部为公开群组.
     * @return array 通过解析REST接口json返回包得到的关联数组，包含拉取到的群组信息、成功与否及错误提示等字段
     */
    public List group_get_appid_group_list2(int limit, int offset, String group_type);

    /**
     * 创建群
     *
     * @param group_type 群类型, 包括Public(公开群), Private(私密群), ChatRoom(聊天室)
     * @param group_name 群名称
     * @param owner_id   群主id, 自动添加到群成员中.如果不填，则群没有群主
     * @return array 通过解析REST接口json返回包得到的关联数组，包含新建的群号、成功与否、错误提示等字段
     */
    public List group_create_group(String group_type, String group_name, String owner_id);

    /**
     * 创建群(高级接口)
     *
     * @param group_type 群类型(包括Public(公开群), Private(私密群), ChatRoom(聊天室))
     * @param group_name 群名称
     * @param owner_id   群主id, 自动添加到群成员中.如果不填，群没有群主
     * @param info_set   存储群组基本信息的字典，内容包括用introduction 群简介, group_id 自定义群组显示出来的id,
     *                   notification 群公告, face_url 群头像url地址, max_member_num 最大群成员数量, apply_join 申请加群处理方式
     *                   (比如FreeAccess 自由加入). php构造示例:
     *                   <p>
     *                   $info_set = array(
     *                   'introduction' => "群简介"(String),
     *                   'group_id' => "自定义群组id"(String),
     *                   'notificatoin' => "群公告"(String),
     *                   'face_url' => "群头像url地址"(String),
     *                   'max_member_num' => 最大群成员数量(int),
     *                   'apply_join' => "申请加群的处理方式"(String)
     *                   );
     * @param mem_list   初始群成员列表，最多500个，每个群成员由Member_Account(用户id), Role(角色, 比如管理员Admin,
     *                   普通成员Member)组成. php构造示例:
     *                   <p>
     *                   $mem_list = array();
     *                   $mem_account = array()(
     *                   "Member_Account" => "bob",   // 成员id
     *                   "Role" => "Admin"   // 赋予该成员的身份，目前备选项只有Admin
     *                   );
     *                   array_push($account_list, $mem_account);  //$mem_account为用户id，需要用户传递
     * @return array 通过解析REST接口json返回包得到的关联数组，包含新建的群号、成功与否、错误提示等字段
     */
    public List group_create_group2(String group_type, String group_name, String owner_id, Map info_set, List mem_list);

    /**
     * 转让群组
     *
     * @param group_id  需要转让的群组id
     * @param new_owner 需要设置的新群主id
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_change_group_owner(String group_id, String new_owner);

    /**
     * 获取群组详细信息
     *
     * @param group_id 需要获取信息的群组id
     * @return array 通过解析REST接口json返回包得到的关联数组，包含拉取到的群组信息(如果成功)、成功与否、错误提示等字段
     */
    public List group_get_group_info(String group_id);

    /**
     * 获取群组详细信息(高级接口)
     *
     * @param group_list         群组集合. php构造示例:
     *                           <p>
     *                           $group_list = array();
     *                           array_push($group_list, "group_id"); //group_id 为群组号码
     * @param base_info_filter   基础信息字段过滤器. php构造示例:
     *                           <p>
     *                           $base_info_filter = array(
     *                           "Type",               //群类型(包括Public(公开群), Private(私密群), ChatRoom(聊天室))
     *                           "Name",               //群名称
     *                           "Introduction",       //群简介
     *                           "Notification",       //群公告
     *                           "FaceUrl",            //群头像url地址
     *                           "CreateTime",         //群组创建时间
     *                           "Owner_Account",      //群主id
     *                           "LastInfoTime",       //最后一次系统通知时间
     *                           "LastMsgTime",        //最后一次消息发送时间
     *                           "MemberNum",          //群组当前成员数目
     *                           "MaxMemberNum",       //群组内最大成员数目
     *                           "ApplyJoinOption"     //申请加群处理方式(比如FreeAccess 自由加入)
     *                           );
     * @param member_info_filter 成员信息字段过滤器, php构造示例:
     *                           <p>
     *                           $member_info_filter = array(
     *                           "Account", 		   // 成员ID
     *                           "Role",			   // 成员身份
     *                           <p>
     *                           "JoinTime",        // 成员加入时间
     *                           "LastSendMsgTime", // 该成员最后一次发送消息时间
     *                           "ShutUpUntil"      // 该成员被禁言直到某时间
     *                           );
     * @param app_define_filter  群组维度的自定义字段过滤器, php构造示例:
     *                           <p>
     *                           $app_define_filter = array(
     *                           "GroupTestData1",  //自定义数据
     *                           );
     * @return array 通过解析REST接口json返回包得到的关联数组，包含拉取到的群组信息(如果成功)、成功与否、错误提示等字段
     */
    public List group_get_group_info2(List group_list, List base_info_filter, List member_info_filter, List app_define_filter);

    /**
     * 获取群组成员详细信息
     *
     * @param group_id 群组id
     * @param limit    最多获取多少个成员, 如果不填, 获取全部成员
     * @param offset   从第几个成员开始获取, 如果不填, 从第一个成员开始获取
     * @return array 通过解析REST接口json返回包得到的关联数组，包含拉取到的群组成员详细信息(如果成功)、成功与否、错误提示等字段
     */
    public List group_get_group_member_info(String group_id, int limit, int offset);

    /**
     * 修改群组名字
     *
     * @param group_id   群组id
     * @param group_name 将其作为群组名字
     * @return String 返回成功与否，及错误提示(如果有错误)
     * array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_modify_group_base_info(String group_id, String group_name);

    /**
     * 修改群组信息(高级接口)
     *
     * @param group_id        群组id
     * @param group_name      群组名字
     * @param info_set        需要修改的群组基本信息的字典集合, 包括群简介，群公告， 群头像url地址，群成员最大数量,
     *                        申请加群方式. php构造示例:
     *                        <p>
     *                        $info_set = array(
     *                        'introduction' => "群简介"(String),
     *                        'notification' => "群公告"(String),
     *                        'face_url' => "群头像url地址(String)",
     *                        'max_member_num' => "群成员最大数量"(String),
     *                        'apply_join' => "申请加入方式"(String)
     *                        );
     * @param app_define_list 自定义字段. php构造示例:
     *                        <p>
     *                        $app_define_list = array();
     *                        //定义自定义字段字典数组
     *                        $app_define_one = array()(
     *                        "Key": "GroupTestData1",  // 需要修改的自定义字段key
     *                        "Value": "NewData"  // 自定义字段的新值
     *                        );
     *                        array_push($app_define_list, $app_define_one);
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_modify_group_base_info2(String group_id, String group_name, Map info_set, List app_define_list);

    /**
     * 增加群组成员
     *
     * @param group_id  要操作的群组id
     * @param member_id 要加入的用户id
     * @param silence   是否静默加入, 0为否， 1为是
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_add_group_member(String group_id, String member_id, int silence);


    /**
     * 删除群组成员
     *
     * @param group_id  要操作的群组id
     * @param member_id 要删除的成员id
     * @param silence   是否静默删除, 0为否，1为是
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_delete_group_member(String group_id, String member_id, int silence);


    /**
     * 修改群成员身份
     *
     * @param group_id   要操作的群组id
     * @param account_id 要操作的用户id
     * @param role       用户身份(Admin/Member)
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_modify_group_member_info(String group_id, String account_id, String role);

    /**
     * 修改群成员资料(高级接口)
     *
     * @param group_id    要操作的群组id
     * @param account_id  用户id
     * @param role        Admin或者Member, 分别为设置/取消管理员, 为null则不改变成员身份
     * @param msg_flag    消息屏蔽类型,比如AcceptAndNotify(接收并提示), 为null则不改变屏蔽类型
     * @param shutup_time 禁言时间
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_modify_group_member_info2(String group_id, String account_id, String role, String msg_flag, int shutup_time);

    /**
     * 解散群
     *
     * @param group_id 群组id
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_destroy_group(String group_id);

    /**
     * 获取某一用户加入的群组
     *
     * @param account_id 用户id
     * @return array 通过解析REST接口json返回包得到的关联数组，包含该用户加入的群的信息(如果成功), 成功与否、错误提示等字段
     */
    public List group_get_joined_group_list(String account_id);

    /**
     * 获取某一用户加入的群组(高级接口)
     *
     * @param account_id       用户id
     * @param group_type       拉取哪种群组形态(Pulic(公开群)/Private(私密群)/ChatRoom(聊天室)),不填为拉取所有
     * @param base_info_filter 基础信息字段过滤器. php构造示例:
     *                         <p>
     *                         $base_info_filter = array(
     *                         "Type",               //群类型(包括Public(公开群), Private(私密群), ChatRoom(聊天室))
     *                         "Name",               //群名称
     *                         "Introduction",       //群简介
     *                         "Notification",       //群公告
     *                         "FaceUrl",            //群头像url地址
     *                         "CreateTime",         //群组创建时间
     *                         "Owner_Account",      //群主id
     *                         "LastInfoTime",       //最后一次系统通知时间
     *                         "LastMsgTime",        //最后一次消息发送时间
     *                         "MemberNum",          //群组当前成员数目
     *                         "MaxMemberNum",       //群组内最大成员数目
     *                         "ApplyJoinOption"     //申请加群处理方式(比如FreeAccess 自由加入, NeedPermission 需要同意)
     *                         );
     * @param self_info_filter 自身在群内的消息过滤器. php构造示例:
     *                         <p>
     *                         $self_info_filter = array(
     *                         "Role",            //群内身份(Amin/Member)
     *                         "JoinTime",        //入群时间
     *                         "MsgFlag",         //消息屏蔽类型
     *                         "UnreadMsgNum"     //未读消息数量
     *                         );
     * @return array 通过解析REST接口json返回包得到的关联数组，包含该用户加入的群的信息(如果成功), 成功与否、错误提示等字段
     */
    public List group_get_joined_group_list2(String account_id, String group_type, List base_info_filter, List self_info_filter);

    /**
     * 查询用户在某个群组中的身份
     *
     * @param group_id  群组id
     * @param member_id 要查询的用户
     * @return array 通过解析REST接口json返回包得到的关联数组，包含该用户在某个群的身份(如果成功), 成功与否、错误提示等字段
     */
    public List group_get_role_in_group(String group_id, String member_id);

    /**
     * 批量禁言/取消禁言
     *
     * @param group_id  群组id
     * @param member_id 要禁言/取消禁言 的用户
     * @param second    表示禁言多少秒, 0表示取消禁言
     * @return array 通过解析REST接口json返回包得到的关联数组，包含该用户在某个群的身份(如果成功), 成功与否、错误提示等字段
     */
    public List group_forbid_send_msg(String group_id, String member_id, int second);

    /**
     * 在某一群组里发普通消息
     *
     * @param account_id   发送消息的用户
     * @param group_id     群组id
     * @param text_content 要发送的信息(均为文本消息)
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_send_group_msg(String account_id, String group_id, String text_content);

    /**
     * 在某一群组里发送图片
     *
     * @param account_id 发送消息的用户
     * @param group_id   群组id
     * @param pic_path   要发送图片的本地路径
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_send_group_msg_pic(String account_id, String group_id, String pic_path);

    /**
     * 在某一群组里发普通消息(高级接口)
     *
     * @param account_id  发送消息的用户
     * @param group_id    群组id
     * @param msg_content 要发送的消息集合，这里包括文本消息和表情消息. php构造示例:
     *                    <p>
     *                    //创建array $msg_content
     *                    $msg_content = array();
     *                    //创建array 所需元素
     *                    $msg_content_text = array(
     *                    'MsgType' => 'TIMTextElem',       //文本类型
     *                    'MsgContent' => array(
     *                    'Text' => "hello",                //"hello" 为文本信息
     *                    )
     *                    <p>
     *                    $msg_content_face = array(
     *                    'MsgType' => 'TIMTextElem',       //表情类型
     *                    'MsgContent' => array(
     *                    'Data' => "abc\u0000\u0001",      //"abc\u0000\u0001" 为图片信息
     *                    )
     *                    <p>
     *                    array_push($msg_content, $msg_content_text);
     *                    array_push($msg_content, $msg_content_face);
     *                    );
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_send_group_msg2(String account_id, String group_id, List msg_content);

    /**
     * 在某一群组发系统消息
     *
     * @param group_id    群组id
     * @param content     系统通知内容，支持二进制数组
     * @param receiver_id 接收者群成员id，为空表示全员下发
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_send_group_system_notification(String group_id, String content, String receiver_id);

    /**
     * 在某一群组发系统消息(高级接口)
     *
     * @param group_id      群组id
     * @param content       系统通知内容，支持二进制数组
     * @param receiver_list 接收此系统提示的用户id集合, 为空表示发送给全员. php构造示例:
     *                      <p>
     *                      $receiver_list = array(
     *                      "peter",
     *                      "leckie"
     *                      )
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_send_group_system_notification2(String group_id, String content, List receiver_list);

    /**
     * 导入群成员(只导入一个成员, 入群时间默认为当前)
     *
     * @param group_id  要操作的群组id
     * @param member_id 要导入的用户id
     * @param role      要导入的用户的身份(现可填值只有Admin)，不填默认为Member
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_import_group_member(String group_id, String member_id, String role);

    /**
     * 导入群成员(批量导入)
     *
     * @param group_id    要操作的群组id
     * @param member_list 要导入的用户id集合，构造示例:
     *                    <p>
     *                    $member_list = array();
     *                    $member_elem = array(
     *                    "Member_Account" => $member_id,
     *                    "Role" => $role
     *                    );
     *                    array_push($member_list, $member_elem);
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_import_group_member2(String group_id, List member_list);

    /**
     * 导入一条群文本消息
     *
     * @param group_id     要操作的群组id
     * @param from_account 该消息发送者
     * @param text         文本消息内容
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_import_group_msg(String group_id, String from_account, String text);

    /**
     * 导入群消息(高级接口, 一次最多导入20条)
     *
     * @param group_id 要操作的群组id
     * @param msg_list 消息集合, 构造方式如下：
     *                 <p>
     *                 //构造MsgBody
     *                 $msg_content = array(
     *                 "Text" => $text
     *                 );
     *                 $msg_body_elem = array(
     *                 "MsgType" => "TIMTextElem",
     *                 "MsgContent" => $msg_content,
     *                 );
     *                 $msg_body_list = array();
     *                 array_push($msg_body_list, $msg_body_elem);
     *                 //构造MsgList的一个元素
     *                 $msg_list_elem = array(
     *                 "From_Account" => $from_account,
     *                 "SendTime" => time(),
     *                 "Random" => rand(1, 65535),
     *                 "MsgBody" => $msg_body_list
     *                 );
     *                 //构造MsgList
     *                 $msg_list = array();
     *                 array_push($msg_list, $msg_list_elem);
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_import_group_msg2(String group_id, List msg_list);

    /**
     * 设置群组成员未读计数
     *
     * @param group_id       要操作的群组id
     * @param member_account 要操作的群成员
     * @param unread_msg_num 该成员的未读计数
     * @return array 通过解析REST接口json返回包得到的关联数组，包含成功与否、错误提示等字段
     */
    public List group_set_unread_msg_num(String group_id, String member_account, int unread_msg_num);


    //################################ 通用接口 ###################################

    /**
     * 直接访问RestApi
     * 建议仅在没有合适接口时才考虑使用
     *
     * @param req_body 相应RestApi所需要的请求包体内容
     * @return array 通过解析REST接口json返回包得到的关联数组, 其中包含成功与否、及错误提示(如果有错误)等字段
     */
    public List comm_rest(String server_name, String command, String req_body);

    /**
     * 判断操作系统位数
     * 64位返回true
     */
    public boolean is_64bit();
}
