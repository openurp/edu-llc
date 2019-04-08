[#ftl]
[@b.head/]
[@b.toolbar title="新增/修改课堂活动"]bar.addBack();[/@]
  <script type="text/javascript" crossorigin="anonymous" src="${base}/static/js/ajax-chosen.js"></script>
[@b.tabs]
  [@b.form theme="list" action=b.rest.save(lecture)]
    [@b.datepicker label="日期" required="true" id="lecture.date" name="lecture.date"
    value="${(lecture.date?string('yyyy-MM-dd'))?default('')}" style="width:200px"  format="yyyy-MM-dd" /]
    [@b.startend label="时间"
      name="lecture.beginAt,lecture.endAt" required="true,true"
      start=(lecture.beginAt)! end=(lecture.endAt)! format="HH:mm" style="width:200px"/]
    [@b.textfield name="lecture.subject" label="活动名称" value="${lecture.subject!}" required="true" maxlength="100" style="width:200px;"/]
    [@b.select name="lecture.depart.id" label="开课院系" value="${(lecture.depart.id)!}" style="width:200px;" items=departments empty="..." required="true"/]
    [@b.field label="教室"]
      <select id="roomId" name="lecture.room.id" style="width:200px;">
        <option value='${(lecture.room.id)!}' selected>${(lecture.room.name)!}</option>
      </select>
    [/@]
    [@b.textfield name="lecture.location" label="地点" value="${lecture.location!}" maxlength="100" style="width:200px;"/]
    [@b.textfield name="lecture.teachers" label="组织教师" value="${lecture.teachers!}" required="true" maxlength="500" style="width:200px;"/]
    [@b.textfield name="lecture.capacity" label="最大容量" value="${lecture.capacity!}" required="true" maxlength="20" style="width:200px;"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[/@]

<script>
  jQuery("#roomId").ajaxChosen(
    {
        method: 'GET',
        url:  "${b.url('!room?pageNo=1&pageSize=10')}"
    }
    , function (data) {
        var items = {};
        var dataObj = eval("(" + data + ")");
        jQuery.each(dataObj.rooms, function (i, room) {
            items[room.id] = room.name ;
        });
        return items;
    },
    {width:"400px"}
  );
</script>
[@b.foot/]
