function login() {
    var username = $("input[name='userName']").val();
    var userpwd = $("input[name='password']").val();
    if(isEmpty(username)){
        alert("请输入用户名")
        return;
    }
    if (isEmpty(userpwd)){
        alert("请输入密码");
        return;
    }

    $.ajax({
      type:"post",
      dataType:"json",
      data:{
          userName:username,
          userPwd:userpwd
      },
      url:ctx+"/user/login",
      success:function (data) {
          if(data.code==200){
              var result = data.result;
              //登录成功
              $.cookie("userIdStr",result.userIdStr);
              $.cookie("userName",result.userName);
              $.cookie("trueName",result.trueName);
              window.location.href=ctx+"/main";
          }else {
              alert(data.msg);
          }
      }
    })


}