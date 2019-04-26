[#ftl]
[@b.head/]
[@b.toolbar title="外语中心介绍"/]
[@b.grid items=introes var="intro"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col  title="外语中心介绍"][@b.a href="!info?id=${intro.id}"]外语中心介绍${intro_index+1}[/@][/@]
  [/@]
[/@]
[@b.foot/]
