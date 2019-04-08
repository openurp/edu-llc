[#ftl]
[@b.head/]
[@b.toolbar title="辅导活动预约名单"/]
<table class="indexpanel">
  <tr>
    <td class="index_view" >
    [@b.form name="tutoredStdSearchForm" action="!search" target="tutoredStdlist" title="ui.searchForm" theme="search"]
      [@b.textfield name="tutoredStd.std.user.code" label="学号"/]
      [@b.textfield name="tutoredStd.std.user.name" label="姓名"/]
      [@b.textfield name="tutoredStd.activity.subject" label="活动名称"/]
      [@b.textfield name="tutoredStd.activity.teacher.user.name" label="教师"/]
      [@b.textfield name="tutoredStd.activity.location" label="地点"/]
      [@b.datepicker label="日期" id="tutoredStd.activity.date" name="tutoredStd.activity.date"
      value="${(tutoredStd.activity.date?string('yyyy-MM-dd'))?default('')}" format="yyyy-MM-dd" /]
      [@b.select label="学年学期" name="tutoredStd.activity.semester.id" items={}]
        <option value="">...</option>
        [#list semesters?sort_by("code")?reverse as semester]
          <option value="${semester.id}" [#if semester.id = currentSemester.id]selected[/#if]>${(semester.schoolYear)!}学年${(semester.name?replace('0','第'))!}学期</option>
        [/#list]
      [/@]
      <input type="hidden" name="orderBy" value="activity.date"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="tutoredStdlist" href="!search?orderBy=activity.date"/]
    </td>
  </tr>
</table>
[@b.foot/]
