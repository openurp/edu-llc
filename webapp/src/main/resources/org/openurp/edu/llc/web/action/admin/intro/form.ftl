[#ftl]
[@b.head/]
[@b.toolbar title="编辑外语中心介绍"]
  bar.addBack("${b.text("action.back")}");
[/@]
[@b.tabs]
  [@b.form theme="list" action=b.rest.save(intro)]
    [@b.textarea name="intro.intro" label="详细介绍" value="${intro.intro!}" maxlength="10000" style="width:700px;height:300px;" id="editor_id"/]
    [@b.formfoot]
    [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[/@]

  <link rel="stylesheet" href="${base}/static/kindeditor/themes/default/default.css"></link>
  <script charset="utf-8" src="${base}/static/kindeditor/kindeditor-all-min.js"></script>
  <script>
     KindEditor.remove('#editor_id');
     var edit_id=KindEditor.create('#editor_id', {
         allowPreviewEmoticons : false,
         allowImageUpload : false,
         items:[
             'undo', 'redo', '|', 'preview','cut', 'copy', 'paste',
             'plainpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
             'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
             'superscript', 'selectall', '|', 'fullscreen', '/',
             'formatblock', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
             'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat'
         ],
        afterBlur: function(){this.sync();}
     });
     edit_id.focus();
  </script>
[@b.foot/]
