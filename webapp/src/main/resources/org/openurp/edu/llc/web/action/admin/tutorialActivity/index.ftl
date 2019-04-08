[#ftl]
[@b.head/]
[@b.toolbar title="辅导活动"/]
<table class="indexpanel">
  <tr>
    <td class="index_view" >
    [@b.form name="tutorialActivitySearchForm" action="!search" target="tutorialActivitylist" title="ui.searchForm" theme="search"]
      [@b.textfield name="tutorialActivity.subject" label="名称"/]
      [@b.textfield name="tutorialActivity.teacher.user.name" label="教师"/]
      [@b.textfield name="tutorialActivity.location" label="地点"/]
      [@b.datepicker label="日期" id="tutorialActivity.date" name="tutorialActivity.date"
      value="${(tutorialActivity.date?string('yyyy-MM-dd'))?default('')}" format="yyyy-MM-dd" /]
      [@b.select label="学年学期" name="tutorialActivity.semester.id" items={}]
        <option value="">...</option>
        [#list semesters?sort_by("code")?reverse as semester]
          <option value="${semester.id}" [#if semester.id = currentSemester.id]selected[/#if]>${(semester.schoolYear)!}学年${(semester.name?replace('0','第'))!}学期</option>
        [/#list]
      [/@]
      [@b.select label="预约状态" name="status" items={}]
        <option value="2">...</option>
        <option value="1">已约满</option>
        <option value="0">未约满</option>
      [/@]
      <input type="hidden" name="orderBy" value="date desc"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="tutorialActivitylist" href="!search?orderBy=date desc"/]
    </td>
  </tr>
</table>
[@b.foot/]
