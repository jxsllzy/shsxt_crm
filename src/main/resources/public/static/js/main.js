function openTab(text, url, iconCls){
    if($("#tabs").tabs("exists",text)){
        $("#tabs").tabs("select",text);
    }else{
        var content="<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='" + url + "'></iframe>";
        $("#tabs").tabs("add",{
            title:text,
            iconCls:iconCls,
            closable:true,
            content:content
        });
    }
}

/**
 * 用户退出
 */
function logout() {
    $.messager.confirm("来自crm","确定退出系统?",function (r){
     if(r){
         $.removeCookie("userIdStr");
         $.removeCookie("userName");
         $.removeCookie("trueName");
         $.messager.alert("来自crm","系统将在3秒后自动退出","info");
         setTimeout(function () {
             window.location.href = ctx+"/index";
         },3000);
     }
    });
}

/**
 * 打开模态框
 */
function openPasswordModifyDialog() {
    $("#dlg").dialog("open").dialog("setTitle","密码修改");
}

/**
 * 确认修改密码
 */
function modifyPassword() {
    $("#fm").form("submit",{
        url:ctx+"/user/updatePassword",
        onSubmit:function () {
            //验证
            return $("#fm").form("validate");
        },
        success:function (data) {
            data = JSON.parse(data);
            if(data.code==200){
                $.removeCookie("userIdStr");
                $.removeCookie("userName");
                $.removeCookie("trueName");
                $.messager.alert("来自crm","密码修改成功，3秒后跳转登录页面","info");
                setTimeout(function () {
                    window.location.href = ctx+"/index";
                },3000);
            }else{
                $.messager.alert("来自crm",data.msg,"error");
            }
        }
    });
}

