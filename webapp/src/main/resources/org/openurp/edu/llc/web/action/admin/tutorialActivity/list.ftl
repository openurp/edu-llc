[#ftl]
[@b.head/]
[@b.grid items=tutorialActivities var="tutorialActivity"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("复制",'copy()');
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="10%" property="date" title="日期"]${(tutorialActivity.date?string('yyyy-MM-dd'))?default('')}[/@]
    [@b.col width="15%" title="时间"]${tutorialActivity.beginAt! }-${tutorialActivity.endAt }[/@]
    [@b.col width="15%" property="subject" title="活动名称（类别）"/]
    [@b.col width="10%" property="teacher.user.name" title="组织教师"/]
    [@b.col width="10%" property="location" title="地点"/]
    [@b.col width="10%" property="capacity" title="最大容量"/]
    [@b.col width="10%" title="实际人数"][@b.a href="!tutoresStds?id=${tutorialActivity.id}"]${(tutorialActivity.stds.size)! }[/@][/@]
    [@b.col width="15%" property="semester" title="学年学期"]${(tutorialActivity.semester.schoolYear)!}学年${(tutorialActivity.semester.name?replace('0','第'))!}学期[/@]
  [/@]
[/@]

<script>
function copy()
{
    var avtivityIds = bg.input.getCheckBoxValues("tutorialActivity.id");
    if(avtivityIds=="" || avtivityIds==null)
      window.alert('你没有选择要操作的记录！');
    else {
      if(avtivityIds.indexOf(',')!=-1){alert("请仅选择一个");return;}
      var week=prompt("请填写复制周数","");
      if(week)
      {
        bg.form.submit("tutorialActivitySearchForm","${b.url('!copy')}"+"?activityId=" + avtivityIds +"&week=" +week);
      }
    }
}
</script>
[@b.foot/]
