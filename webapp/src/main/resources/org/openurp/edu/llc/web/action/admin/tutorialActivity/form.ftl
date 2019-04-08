[#ftl]
[@b.head/]
[@b.toolbar title="新增/修改辅导活动"]bar.addBack();[/@]
  <script type="text/javascript" crossorigin="anonymous" src="${base}/static/js/ajax-chosen.js"></script>
[@b.tabs]
  [@b.form theme="list" action=b.rest.save(tutorialActivity)]
    [@b.datepicker label="日期" required="true" id="tutorialActivity.date" name="tutorialActivity.date"
    value="${(tutorialActivity.date?string('yyyy-MM-dd'))?default('')}" style="width:200px"  format="yyyy-MM-dd" /]
    [@b.startend label="时间"
      name="tutorialActivity.beginAt,tutorialActivity.endAt" required="true,true"
      start=(tutorialActivity.beginAt)! end=(tutorialActivity.endAt)! format="HH:mm" style="width:200px"/]
    [@b.textfield name="tutorialActivity.subject" label="活动名称" value="${tutorialActivity.subject!}" required="true" maxlength="100" style="width:200px;"/]
    [@b.textfield name="tutorialActivity.location" label="地点" value="${tutorialActivity.location!}" maxlength="100" style="width:200px;" required="true"/]
    [@b.field label="指导教师"]
      <select id="teacherId" name="tutorialActivity.teacher.id" style="width:200px;">
        <option value='${(tutorialActivity.teacher.id)!}' selected>${(tutorialActivity.teacher.user.name)!}</option>
      </select>
    [/@]
    [@b.textfield name="tutorialActivity.capacity" label="最大容量" value="${tutorialActivity.capacity!}" required="true" maxlength="20" style="width:200px;"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[/@]

<script>
  jQuery("#teacherId").ajaxChosen(
        {
            method: 'GET',
            url:  "${b.url('!teacher?pageNo=1&pageSize=10')}"
        }
        , function (data) {
            var items = {};
            var dataObj = eval("(" + data + ")");
            jQuery.each(dataObj.teachers, function (i, teacher) {
                items[teacher.id] = teacher.name + "(" + teacher.code + ")";
            });
            return items;
        },
        {width:"400px"}
      );
</script>
[@b.foot/]
