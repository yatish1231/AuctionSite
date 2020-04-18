<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Create an account</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

<div class="container">

    <form:form name="regForm" method="POST" modelAttribute="userForm" onsubmit="return validate()" class="form-signin">
        <h2 class="form-signin-heading">Create your account</h2>
        
        <spring:bind path="firstname">
            <div class="form-group">
                <form:input type="text" path="firstname" class="form-control"
                            placeholder="Firstname"></form:input>
            </div>
        </spring:bind>
        
        <spring:bind path="lastname">
            <div class="form-group">
                <form:input type="text" path="lastname" class="form-control"
                            placeholder="Last name"></form:input>
            </div>
        </spring:bind>
        
        <spring:bind path="username">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="username" class="form-control" placeholder="Username"
                            autofocus="true"></form:input>
                <form:errors path="username"></form:errors>
            </div>
        </spring:bind>

        <spring:bind path="password">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="password" path="password" class="form-control" placeholder="Password"></form:input>
                <form:errors path="password"></form:errors>
            </div>
        </spring:bind>
        <input class="form-control" type="password" name="passCheck" placeholder="Re-enter password"/><br>
		<input class="form-group" type="radio" name="type" id="typeSelect" value="ROLE_SELLER"/>Seller 
		<input class="form-group" type="radio" name="type" id="typeSelect" value="ROLE_BUYER"/>Buyer <br/>
		
		<input class="form-control" type="text" id="social" name="social_id" placeholder="Social Id" disabled/>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Submit</button>
    </form:form>

</div>
<!-- /container -->
<script type="text/javascript">
		var nameField = document.getElementById("typeSelect");
		var social = document.getElementById("social");
		nameField.addEventListener("click", function(){
		  
		  if(document.getElementById("typeSelect").value === "ROLE_SELLER"){
		    social.disabled = false;
		  }
		  else{
			  social.disabled = true;
		  }
		});
</script>
<script type = "text/javascript">

      function validate() {
      var x = document.forms["regForm"]["firstname"].value;
      var y = document.forms["regForm"]["lastname"].value;
      var z = document.forms["regForm"]["username"].value;
      var c = document.forms["regForm"]["password"].value;
      var d = document.forms["regForm"]["type"].value;
      var e = document.forms["regForm"]["passCheck"].value;
      var f = document.forms["regForm"]["social_id"].value;
      
         if(x  == "" && y == "" && z == "" && c == "") {
            alert( "Please fill all fields" );
            document.regForm.firstname.focus();
            return false;
         }
         if(x  == "") {
             alert( "Please enter first name" );
             document.regForm.firstname.focus() ;
             return false;
          }
         if(y == "") {
             alert( "Please enter last name" );
             document.regForm.lastname.focus() ;
             return false;
          }
         if(z == "") {
              alert( "Please choose a user-name " );
              document.regForm.username.focus() ;
              return false;
           }
          if(c == "") {
              alert( "Please choose a password" );
              document.regForm.password.focus() ;
              return false;
           }
          if(e == "") {
              alert( "Please re enter password" );
              document.regForm.passCheck.focus() ;
              return false;
           }
          if(c != e) {
              alert( "Passwords don't match!" );
              document.regForm.passCheck.focus() ;
              return false;
           }
          if(d == ""){
        	  alert("Please Select a role!")
        	  document.regForm.type.focus() ;
              return false;          
          }
          if( d == "ROLE_SELLER"){
        		if( f == ""){
        			alert("Seller role - Please enter your social id")
              	  	document.regForm.social_id.focus() ;
                    return false;
        		}  
          }
         return true ;
      }
</script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>