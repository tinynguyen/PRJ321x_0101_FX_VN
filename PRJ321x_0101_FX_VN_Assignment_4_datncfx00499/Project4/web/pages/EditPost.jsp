<%-- 
    Document   : EditPost
    Created on : May 27, 2019, 8:32:45 PM
    Author     : tiny
--%>

<%@page import="com.bean.Users"%>
<%@page import="com.bean.Post"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
  Users user = (Users) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect("blogs");
  } else {
    Post post = (Post) request.getAttribute("post");
    String content = post.getContent().replace("\'", "\"");
%>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Edit Post | <%=post.getTitle()%></title>

    <%-- Meta tags --%>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <%-- Customize CSS --%>
    <link rel="stylesheet" href="./css/style.css">

    <%-- Bootstrap CSS --%>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

    <%-- Google Fonts --%>
    <link href="https://fonts.googleapis.com/css?family=Roboto+Mono&display=swap" rel="stylesheet">

    <%-- Font Awesome Icon --%>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css" integrity="sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay" crossorigin="anonymous">

  </head>
  <body onload="enableEditMode();">

    <%-- Include navigation bar --%>
    <%@include file="../components/navbar.jsp" %>

    <div class="container-fluid mt-5 mb-5">
      <div class="container pt-5 pb-5 shadow rounded">
        <nav aria-label="breadcrumb">
          <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="blogs" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item">Edit Post</li>
            <li class="breadcrumb-item active" aria-current="page"><%= post.getTitle()%></li>
          </ol>
        </nav>
        <div class="row">
          <div class="col-12 col-sm-12 col-md-12 col-lg-12">
            <button onclick="execCmd('bold');" class="m-1"><i class="fas fa-bold m-1"></i></button>
            <button onclick="execCmd('italic');" class="m-1"><i class="fas fa-italic m-1"></i></button>
            <button onclick="execCmd('underline');" class="m-1"><i class="fas fa-underline m-1"></i></button>
            <button onclick="execCmd('strikethrough');" class="m-1"><i class="fas fa-strikethrough m-1"></i></button>
            <button onclick="execCmd('justifyLeft');" class="m-1"><i class="fas fa-align-left m-1"></i></button>
            <button onclick="execCmd('justifyCenter');" class="m-1"><i class="fas fa-align-center m-1"></i></button>
            <button onclick="execCmd('justifyRight');" class="m-1"><i class="fas fa-align-right m-1"></i></button>
            <button onclick="execCmd('justifyFull');" class="m-1"><i class="fas fa-align-justify m-1"></i></button>
            <button onclick="execCmd('cut');" class="m-1"><i class="fas fa-cut m-1"></i></button>
            <button onclick="execCmd('copy');" class="m-1"><i class="fas fa-copy m-1"></i></button>
            <button onclick="execCmd('indent');" class="m-1"><i class="fas fa-indent m-1"></i></button>
            <button onclick="execCmd('outdent');" class="m-1"><i class="fas fa-outdent m-1"></i></button>
            <button onclick="execCmd('subscript');" class="m-1"><i class="fas fa-subscript m-1"></i></button>
            <button onclick="execCmd('superscript');" class="m-1"><i class="fas fa-superscript m-1"></i></button>
            <button onclick="execCmd('undo');" class="m-1"><i class="fas fa-undo m-1"></i></button>
            <button onclick="execCmd('redo');" class="m-1"><i class="fas fa-redo m-1"></i></button>
            <button onclick="execCmd('insertUnorderedList');" class="m-1"><i class="fas fa-list-ul m-1"></i></button>
            <button onclick="execCmd('insertOrderedList');" class="m-1"><i class="fas fa-list-ol m-1"></i></button>
            <button onclick="execCmd('insertParagraph');" class="m-1"><i class="fas fa-paragraph m-1"></i></button>
            <button onclick="execCmd('insertHorizontalRule');" class="">HR</button>
            <button onclick="execCmdWithArg('createLink', prompt('Enter a URL:', 'http://'));" class="m-1"><i class="fas fa-link"></i></button>
            <button onclick="execCmd('unlink');" class="m-1"><i class="fas fa-unlink m-1"></i></button>
          </div>
          <div class="col-12 col-sm-12 col-md-12 col-lg-12">
            <select onchange="execCmdWithArg('formatBlock', this.value)" class="p-1">
              <option value="H1">H1</option>
              <option value="H2">H2</option>
              <option value="H3">H3</option>
              <option value="H4">H4</option>
              <option value="H5">H5</option>
              <option value="H6">H6</option>
            </select>
            <select onchange="execCmdWithArg('fontName', this.value)" class="p-1">
              <option value="Time New Roman">Time New Roman</option>
              <option value="Arial">Arial</option>
              <option value="Comic Sans MS">Comic Sans MS</option>
              <option value="Georgia">Georgia</option>
              <option value="Courier">Courier</option>
              <option value="Roboto">Roboto</option>
            </select>
            <select onchange="execCmdWithArg('fontSize', this.value)" class="p-1">
              <option value="1">1</option>
              <option value="2">2</option>
              <option value="3">3</option>
              <option value="4">4</option>
              <option value="5">5</option>
              <option value="6">6</option>
              <option value="7">7</option>
            </select>
            <input type="color" onchange="execCmdWithArg('foreColor', this.value)"/>
            <input type="color" onchange="execCmdWithArg('hiliteColor', this.value)"/>
            <button onclick="toggleSource();" class="m-1"><i class="fas fa-code m-1"></i></button>
            <button onclick="execCmdWithArg('insertImage', prompt('Enter the image URL:', ''));" class="m-1"><i class="fa fa-file-alt m-1"></i></button>
            <button onclick="execCmd('selectAll');">Select All</button>
            <button onclick="toggleEdit();">Lock Edit</button>
          </div>
        </div>

        <!-- Form -->
        <div class="row">
          <div class="col-12 col-sm-12 col-md-12 col-lg-12">
            <form id="editPost" method="post" action="controller">
              <input type="hidden" name="action" value="editpost" ></input>
              <div class="form-group mt-3 font-weight-bold">
                <label for="title">Title</label>
                <input type="text" name="title" value="<%= post.getTitle()%>" id="title" class="form-control" required="required"></input>
              </div>
              <div class="form-group mt-3 font-weight-bold">
                <label for="description">Description</label>
                <input type="text" name="description" value="<%= post.getDesc()%>" id="description" class="form-control" required="required"></input>
              </div>
              <div class="form-group">
                <label for="category">Example select</label>
                <select class="form-control" id="category" name="category">
                  <option value="Java">Java</option>
                  <option value="JavaScript">JavaScript</option>
                </select>
              </div>
              <div class="form-group mt-3 font-weight-bold">
                <input type="hidden" name="postID" value="<%=post.getPostID()%>"></input>
                <input type="hidden" name="content"></input>
                <label for="richTextField">Content</label>
                <iframe class="form-control" name="richTextField" style="width: 100%; height: 450px" id="richTextField"  srcdoc='<%= content%>'></iframe>
              </div>
            </form>
            <button onclick="edit();" class="btn btn-primary">Submit</button>
            <div class="float-right">
              <span>Published </span><input type="checkbox" disabled="true" checked="true"/>
            </div>
          </div>
        </div>
      </div>
    </div>

    <%-- Include navigation bar --%>
    <%@include file="../components/footer.html" %>

    <!-- Scripts text editor -->
    <script type="text/javascript">
      var showingSourceCode = false;
      var isInEditMode = true;

      function enableEditMode() {
        richTextField.document.designMode = 'On';
      }

      function execCmd(command) {
        richTextField.document.execCommand(command, false, null);
      }

      function execCmdWithArg(command, arg) {
        richTextField.document.execCommand(command, false, arg);
      }

      function toggleSource() {
        if (showingSourceCode) {
          richTextField.document.getElementsByTagName('body')[0].innerHTML = richTextField.document.getElementsByTagName('body')[0].textContent;
          showingSourceCode = false;
        } else {
          richTextField.document.getElementsByTagName('body')[0].textContent = richTextField.document.getElementsByTagName('body')[0].innerHTML;
          showingSourceCode = true;
        }
      }

      function toggleEdit() {
        if (isInEditMode) {
          richTextField.document.designMode = 'Off';
          isInEditMode = false;
        } else {
          richTextField.document.designMode = 'On';
          isInEditMode = true;
        }
      }

      function edit() {
        var content = document.getElementById('richTextField').contentDocument.body.innerHTML || document.getElementById('richTextField').contentWindow.body.innerHTML;
        var form = document.getElementById("editPost");
        form.elements['content'].value = content;
        form.submit();
      }
    </script>

  </body>
</html>
<% }%>