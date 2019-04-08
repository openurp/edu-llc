[#ftl]
[@b.head/]
[@b.toolbar title="新增/修改辅导活动开关"]bar.addBack();[/@]
  <script type="text/javascript" crossorigin="anonymous" src="${base}/static/js/ajax-chosen.js"></script>
[@b.tabs]
  [@b.form theme="list" action=b.rest.save(tutorialSwitch)]
    [@b.textfield name="tutorialSwitch.name" label="开关名称" value="${tutorialSwitch.name!}" required="true" maxlength="100" style="width:200px;"/]
    [@b.startend label="时间"
      name="tutorialSwitch.beginAt,tutorialSwitch.endAt" required="true,true"
      start=(tutorialSwitch.beginAt)! end=(tutorialSwitch.endAt)! format="yyyy-MM-dd HH:mm" style="width:200px"/]
    [@b.radios label="开关状态" name="tutorialSwitch.opened" value=tutorialSwitch.opened items="1:打开,0:关闭" required="true"/]
    [@b.textarea name="tutorialSwitch.remark" label="注意事项" value="${tutorialSwitch.remark!}" maxlength="2000" cols="150" rows="20" /]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[/@]
[@b.foot/]
