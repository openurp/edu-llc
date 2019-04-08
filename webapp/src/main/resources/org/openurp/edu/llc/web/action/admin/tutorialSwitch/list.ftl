[#ftl]
[@b.head/]
[@b.grid items=tutorialSwitches var="tutorialSwitch"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="15%" property="name" title="名称"/]
    [@b.col width="30%" property="beginAt" title="开始时间"]${tutorialSwitch.beginAt?string("yyyy-MM-dd HH:mm") }[/@]
    [@b.col width="30%" property="endAt" title="结束时间"]${(tutorialSwitch.endAt ?string("yyyy-MM-dd HH:mm"))!}[/@]
    [@b.col width="20%" property="opened" title="开关状态"]${tutorialSwitch.opened?string('开启','关闭') }[/@]
  [/@]
[/@]
[@b.foot/]
