<!DOCTYPE html>
<%@page import="java.util.Date"%>
<%@page import="org.springframework.web.context.request.RequestScope"%>
<html>
<head>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<title>View Bids</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<style>
body,h1,h2,h3,h4,h5,h6 {font-family: Arial, Helvetica, sans-serif}
</style>

</head>
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
    <p class="w3-text-grey">Buyer</p>
  </div>
  <div class="w3-bar-block">
    <a href="${contextPath}/buyer/auction/viewall" onclick="" class="w3-bar-item w3-button w3-padding w3-text-teal"><i class="fa fa-th-large fa-fw w3-margin-right"></i>View Live Auctions</a> 
    <a href="${contextPath}/buyer/auction/view/bids" onclick="w3_close()" class="w3-bar-item w3-button w3-padding"><i class="fa fa-user fa-fw w3-margin-right"></i>View my Bids</a> 
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
  <c:if test="${requestScope.message != null}"><div class="w3-panel w3-yellow"><h2>${requestScope.message}</h2></div></c:if>
  <div class="w3-container w3-centre w3-padding-large">
  <c:if test="${requestScope.auction != null}">
  		<div class="w3-cell-row">
  		<div class="w3-cell w3-padding" style="width:50%">
  		<img src="/imagefolder/${requestScope.auction.product.filepath}" style="width:100%;max-width:500px" class="w3-hover-opacity w3-card-4">
  		</div>
  		<div class="w3-cell w3-padding"> 
		<div class="w3-left-align"><h2>${requestScope.auction.product.name}</h2></div>
		<div class="w3-left-align"><h4>Product Id: <b>${requestScope.auction.product.id}</b></h4></div>
		<div class="w3-left-align"><h4>Seller: <b>${requestScope.auction.product.seller.username}</b></h4></div>
		<div class="w3-left-align"><h4>Starting Price: <b>${requestScope.auction.product.price}</b></h4></div>
		<div class="w3-left-align"><h4>Auction started on: <b>${requestScope.auction.start_time}</b></h4></div>
		<div class="w3-left-align"><h4>Auction ends in: <b id="auctionTimer"></b></h4></div>
		<script type="text/javascript"> var end_time_js = "${requestScope.auction.end_time}"</script>
		<script type="text/javascript" src="${contextPath}/resources/js/countDownTimer.js"></script>
		<div class="w3-panel w3-green"><h4>Current Highest Bid: <b>${requestScope.currentMaxBid.price}</b></h4></div>
		<div class="w3-container">
		<form:form name="mybidform" method="POST" modelAttribute="bidObj" onsubmit="return validate()" action="${contextPath}/buyer/auction/bid/${requestScope.auction.product.name}/${requestScope.auction.product.id}" class="w3-container w3-centre w3-padding-large">
        <h3 class="w3-centre">Place Bid</h3>
        <spring:bind path="price">
            <div>
                <form:input class="w3-input" type="text" path="price"
                            placeholder="Your Bid $$$" ></form:input>
                <form:errors path="price"></form:errors>            
            </div>
        </spring:bind>
        <spring:bind path="id" >
                <form:input type="hidden" path="id"
                           value="${requestScope.auction.id}" ></form:input>
                <form:errors path="id"></form:errors>
        </spring:bind>
        <spring:bind path="time" >
                <form:input type="hidden" path="time"
                           value="<%= new java.util.Date() %>" ></form:input>
                <form:errors path="time"></form:errors>
        </spring:bind>
     <button class="w3-input w3-btn" type="submit" name="placeBid">Place Bid</button>
	</form:form>
	</div>
	</div>
	</div>
	<table class="w3-table-all">
		<thead>
		 <tr class="w3-light-grey">
			<th>Bidder name</th>
			<th>Time</th>
			<th>Price</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="bid" items="${requestScope.placedBids}">
			<tr>
			<td>${bid.user.username}</td>
			<td>${bid.bid_time}</td>
			<td>${bid.price}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	</c:if>
	</div>
  <!-- Pagination -->
  <div class="w3-center w3-padding-32">
    <div class="w3-bar">
      <a href="#" class="w3-bar-item w3-button w3-hover-black">«</a>
      <a href="#" class="w3-bar-item w3-black w3-button">1</a>
      <a href="#" class="w3-bar-item w3-button w3-hover-black">2</a>
      <a href="#" class="w3-bar-item w3-button w3-hover-black">3</a>
      <a href="#" class="w3-bar-item w3-button w3-hover-black">4</a>
      <a href="#" class="w3-bar-item w3-button w3-hover-black">»</a>
    </div>
  </div>

  <div class="w3-container w3-padding-large" style="margin-bottom:32px">
    <h4><b>About the Website</b></h4>
    <p></p>
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
      var x = document.forms["mybidform"]["price"].value;
         if( x  == "" ) {
            alert( "Please enter a price" );
            document.mybidform.price.focus() ;
            return false;
         }
         if(isNaN(x)){
             alert( "Price should be a number!" );
             document.mybidform.price.focus() ;
             return false;
          }
         if( x < ${requestScope.auction.product.price} ) {
            alert( "Price below minimum" );
            document.mybidform.price.focus() ;
            return false;
         }
         return true ;
      }
</script>
</body>
</html>
