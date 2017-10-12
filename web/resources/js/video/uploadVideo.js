/**
 *  @Description: TODO
 * @author zhiwei.yan
 * @date:2017-08-20 00:43.
 */
$(function(){
    var projectName = (window.document.location.pathname).substring(0, (window.document.location.pathname).substr(1).indexOf('/') + 1);
    $('form').on('submit', function() {
        $(this).ajaxSubmit({
            type: 'post', // 提交方式 get/post
            url: projectName + '/saveVideo.jhtml', // 需要提交的 url
            enctype:"multipart/form-data",
            dataType:"json",
            beforeSubmit:function() {

            },
            success: function(result) { // data 保存提交后返回的数据，一般为 json 数据
                if (result && result.code == 1) {
                    alert("保存成功")
                }
            },
            error:function() {
                alert("提交失败!");
                return false;
            }
        });
        return false; // 阻止表单自动提交事件
    });
});