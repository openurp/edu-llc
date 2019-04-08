[#ftl]
[@b.head/]
[@b.toolbar title="课堂活动"/]
<table class="indexpanel">
  <tr>
    <td class="index_view" >
    [@b.form name="lectureSearchForm" action="!search" target="lecturelist" title="ui.searchForm" theme="search"]
      [@b.textfield name="lecture.subject" label="名称"/]
      [@b.textfield name="lecture.teachers" label="教师"/]
      [@b.textfield name="lecture.location" label="地点"/]
      [@b.datepicker label="日期" id="lecture.date" name="lecture.date"
      value="${(lecture.date?string('yyyy-MM-dd'))?default('')}" format="yyyy-MM-dd" /]
      [@b.select label="学年学期" name="lecture.semester.id" items={}]
        <option value="">...</option>
        [#list semesters?sort_by("code")?reverse as semester]
          <option value="${semester.id}" [#if semester.id = currentSemester.id]selected[/#if]>${(semester.schoolYear)!}学年${(semester.name?replace('0','第'))!}学期</option>
        [/#list]
      [/@]
      [@b.select name="lecture.depart.id" label="开课院系" value="${(lecture.depart.id)!}" items=departments empty="..."/]
      [@b.select label="预约状态" name="status" items={}]
        <option value="2">...</option>
        <option value="1">已约满</option>
        <option value="0">未约满</option>
      [/@]
      <input type="hidden" name="orderBy" value="date desc"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="lecturelist" href="!search?orderBy=date desc"/]
    </td>
  </tr>
</table>
[@b.foot/]
