/**
 *  @Description: TODO
 * @author zhiwei.yan
 * @date:2017-08-20 22:11.
 */
$(function(){
    $('[name="preview"]').on("click", function() {
        $(this).closest("td").find("video").show();
        $(this).closest("td").find("video").trigger("play");
    });

    $('[name="close"]').on("click", function() {
       $(this).closest("td").find("video").hide();
        $(this).closest("td").find("video").trigger("pause");
    });
});