function searchUsers() {
    var userName=$("#s_userName").val();
    var phone = $("#s_phone").val();
    var trueName =$("#s_trueName").val();
    $("#dg").datagrid("load",{
        userName:userName,
        phone:phone,
        trueName:trueName
    })
}

function openUserAddDialog(){
    openAddDialog("dlg","添加用户数据");
}

function clearFormData() {
    $("#userName").val("");
    $("#trueName").val("");
    $("#email").val("");
    $("#phone").val("");
}


function closeUserDialog() {
    closeDialog("dlg");
}

function saveOrUpdateUser() {
    saveOrUpdate("/user/save","/user/update","dlg",searchUsers,clearFormData);
}

function openUserModifyDialog(){
    var rows=$("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选择待修改的数据!","error");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","暂不支持批量修改!","error");
        return;
    }
    rows[0].roleIds = rows[0].rids.split(",");
    $("#fm").form("load",rows[0]);
    openAddDialog("dlg","更新数据");
}

function deleteUser() {
    deleteRecode("dg","/user/delete",searchUsers);
}