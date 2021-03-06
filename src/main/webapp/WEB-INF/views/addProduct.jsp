<!DOCTYPE html>
<html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<title>Seller Add Products</title>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}"/>
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/common.css" rel="stylesheet">
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<style>
body,h1,h2,h3,h4,h5,h6 {font-family: "Raleway", sans-serif}
</style>
<body class="w3-light-grey w3-content" style="max-width:1600px">

<c:if test="${pageContext.request.userPrincipal.name != null}">    	
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
 </c:if>

<!-- Sidebar/menu -->
<nav class="w3-sidebar w3-collapse w3-white w3-animate-left" style="z-index:3;width:300px;" id="mySidebar"><br>
  <div class="w3-container">
    <a href="#" onclick="w3_close()" class="w3-hide-large w3-right w3-jumbo w3-padding w3-hover-grey" title="close menu">
      <i class="fa fa-remove"></i>
    </a>
    <h4><b>${pageContext.request.userPrincipal.name}</b></h4>
    <p class="w3-text-grey">Seller</p>
  </div>
  <div class="w3-bar-block">
    <a href="${contextPath}/seller/products/view" onclick="w3_close()" class="w3-bar-item w3-button w3-padding w3-text-teal"><i class="fa fa-th-large fa-fw w3-margin-right"></i>View Products</a> 
    <a href="${contextPath}/seller/products/add" onclick="w3_close()" class="w3-bar-item w3-button w3-padding"><i class="fa fa-user fa-fw w3-margin-right"></i>Add Products</a> 
    <a href="#contact" onclick="document.forms['logoutForm'].submit()" class="w3-bar-item w3-button w3-padding"><i class="fa fa-envelope fa-fw w3-margin-right"></i>Logout</a>
  </div>
</nav>

<!-- Overlay effect when opening sidebar on small screens -->
<div class="w3-overlay w3-hide-large w3-animate-opacity" onclick="w3_close()" style="cursor:pointer" title="close side menu" id="myOverlay"></div>

<!-- !PAGE CONTENT! -->
<div class="w3-main" style="margin-left:300px">

  <!-- Header -->
  <header id="portfolio">
    <a href="#"><img src="/w3images/avatar_g2.jpg" style="width:65px;" class="w3-circle w3-right w3-margin w3-hide-large w3-hover-opacity"></a>
    <span class="w3-button w3-hide-large w3-xxlarge w3-hover-text-grey" onclick="w3_open()"><i class="fa fa-bars"></i></span>
    <div class="w3-container">
    <h1><b>Live Auction Website</b></h1>
    <div class="w3-section w3-bottombar w3-padding-16">
      
    </div>
    </div>
  </header>
  
  <div class="w3-container w3-centre w3-padding-large">

    <form:form name="productForm" method="POST" modelAttribute="product" class="form-signin" onsubmit="return validate()" enctype="multipart/form-data">
        <h2 class="form-signin-heading">Add your product</h2>
        
        <spring:bind path="name">
            <div class="w3-centre">
                <form:input type="text" path="name" class="w3-input"
                            placeholder="Name" ></form:input>
                            <form:errors path="name"></form:errors>
            </div>
        </spring:bind>
        
        <spring:bind path="price">
            <div class="w3-centre">
                <form:input type="text" path="price" class="w3-input"
                            placeholder="Price:" ></form:input>
                            <form:errors path="price"></form:errors>
            </div>
        </spring:bind>
        <spring:bind path="photo">
            <div class="w3-centre">
                <form:input type="file" path="photo" class="w3-input"
                            placeholder="Picture:" ></form:input>
                            <form:errors path="photo"></form:errors>
            </div>
        </spring:bind>
        <div class="w3-input">
        <c:forEach var="type" items="${requestScope.categories}">
        <input  type="radio" name="cat" value="${type.name}" /> ${type.name} |
        </c:forEach>
        </div>
        <input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
        <button class="w3-input w3-btn" type="submit">Submit</button>
    </form:form>
  </div>
  
  <div class="w3-container w3-padding-large" style="margin-bottom:32px">
    <h4><b>About the Website</b></h4>
    <p>Live Auction Website by Yatish Pitta</p>
    <hr>
    
    <h4>Sales--</h4>
    <!-- Progress bars / Skills -->
    <p>Percentage sales</p>
    <div class="w3-grey">
      <div class="w3-container w3-dark-grey w3-padding w3-center" style="width:65%">65%</div>
    </div>
    <div>
   <a href="${contextPath}/seller/auction/viewall" class="w3-bar-item w3-button w3-padding"><i class="fa fa-user fa-fw w3-margin-right"></i>View Live Auctions</a>
   </div>
    <hr>

  <!-- Footer -->
  <footer class="w3-container w3-padding-32 w3-dark-grey">
  <div class="w3-row-padding">
    <div class="w3-third">
      <h3>Terms and Conditions</h3>
      <p>User terms and conditions</p>
      <p>2020 <a href="https://github.com/yatish1231/AuctionSite.git" target="_blank">Auction Website</a></p>
    </div>
  

    <div class="w3-third">
      <h3>POPULAR ITEMS</h3>
      <p>
        <span class="w3-tag w3-black w3-margin-bottom">Travel</span>
      </p>
    </div>

  </div>
  </footer>
  
<div class="w3-black w3-center w3-padding-24">Project by <a href="https://github.com/yatish1231/AuctionSite.git" target="_blank" class="w3-hover-opacity">Yatish Pitta</a></div>
<!-- End page content -->
</div>
</div>

<script>
// Script to open and close sidebar
function w3_open() {
    document.getElementById("mySidebar").style.display = "block";
    document.getElementById("myOverlay").style.display = "block";
}
 
function w3_close() {
    document.getElementById("mySidebar").style.display = "none";
    document.getElementById("myOverlay").style.display = "none";
}
</script>
<script type = "text/javascript">

      function validate() {
      var x = document.forms["productForm"]["price"].value;
      var y = document.forms["productForm"]["name"].value;
      var z = document.forms["productForm"]["photo"].value;
      var c = document.forms["productForm"]["cat"].value;
         if(x  == "" && y == "" && z == "" && c == "") {
            alert( "Please fill all fields" );
            document.productForm.name.focus();
            return false;
         }
         if(x  == "") {
             alert( "Please enter a price" );
             document.productForm.price.focus() ;
             return false;
          }
         if(isNaN(x)){
             alert( "Price should be a number!" );
             document.productForm.price.focus() ;
             return false;
          }
         if(y == "") {
             alert( "Please enter a name" );
             document.productForm.name.focus() ;
             return false;
          }
         if(z == "") {
              alert( "Please add a photo" );
              document.productForm.photo.focus() ;
              return false;
           }
          if(c == "") {
              alert( "Please enter a category" );
              document.productForm.price.focus() ;
              return false;
           }
         return true ;
      }
</script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>