
<%@page contentType="text/html" pageEncoding="UTF-8"%>
 <%@page import="org.sap.model.Offence"%>
 <%@page import="org.sap.service.ODataService"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="sidebar.jsp"%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div class="page-container">
         <div class="main-content">
                <div class="section__content section__content--p30">
                    <div class="container-fluid">
                        

                        <div class="row">
                            <div class="col-lg-12">
                                <h2 class="title-1 m-b-25">Recent Offences</h2>
                                <div class="table-responsive table--no-card m-b-40">
                                    <table class="table table-borderless table-striped table-earning">
                                        <thead>
                                            <tr>
                                                <th>date</th>
                                                <th>car plate</th>
                                                <th>driver name</th>
                                                <th class="text-right">Area</th>
                                                <th class="text-right">Offences</th>
                                                <th class="text-right">Penalty</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <%
										  	ArrayList<Offence> offences = ODataService.readOffencesInfoFromOdata();
                                        	for(Offence offence : offences){
                                     	%>
                                     	
                                            <tr>
                                                <td><%=offence.getTimestamp()%></td>
                                                <td><%=offence.getPlateNumber()%></td>
                                                <td><%=offence.getDriverName()%></td>
                                                <td class="text-right"><%=offence.getLocation()%></td>
                                                <td class="text-right">Bus Box</td>
                                                <td class="text-right">$150.00</td>
                                            </tr>
                                        <% 
                                        	}
										  %>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-6">
                                <div class="au-card recent-report">
                                    <div class="au-card-inner">
                                        <h3 class="title-2">Monthly offences</h3>
                                        <div class="chart-info">
                                            <div class="chart-info__left">
                                                <div class="chart-note">
                                                    <span class="dot dot--blue"></span>
                                                    <span>Past Year</span>
                                                </div>
                                                <div class="chart-note mr-0">
                                                    <span class="dot dot--green"></span>
                                                    <span>This Year</span>
                                                </div>
                                            </div>
<!--                                            <div class="chart-info__right">
                                                <div class="chart-statis">
                                                    <span class="index incre">
                                                        <i class="zmdi zmdi-long-arrow-up"></i>25%</span>
                                                    <span class="label">products</span>
                                                </div>
                                                <div class="chart-statis mr-0">
                                                    <span class="index decre">
                                                        <i class="zmdi zmdi-long-arrow-down"></i>10%</span>
                                                    <span class="label">services</span>
                                                </div>
                                            </div>-->
                                        </div>
                                        <div class="recent-report__chart">
                                            <canvas id="recent-rep-chart"></canvas>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-6">
                                <div class="au-card chart-percent-card">
                                    <div class="au-card-inner">
                                        <h3 class="title-2 tm-b-5">Offences based on area</h3>
                                        <div class="row no-gutters">
                                            <div class="col-xl-3">
                                                <div class="chart-note-wrap">
                                                    <div class="chart-note mr-0 d-block">
                                                        <span class="dot dot--blue"></span>
                                                        <span>North</span>
                                                    </div>
                                                    <div class="chart-note mr-0 d-block">
                                                        <span class="dot dot--red"></span>
                                                        <span>East</span>
                                                    </div>
                                                    <div class="chart-note mr-0 d-block">
                                                        <span class="dot dot--green"></span>
                                                        <span>South</span>
                                                    </div>
                                                    <div class="chart-note mr-0 d-block">
                                                        <span class="dot dot--yellow"></span>
                                                        <span>West</span>
                                                    </div>
                                                    <div class="chart-note mr-0 d-block">
                                                        <span class="dot dot--purple"></span>
                                                        <span>Central</span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xl-7">
                                                <div class="percent-chart">
                                                    <canvas id="percent-chart"></canvas>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="js/main.js"></script>
    </body>
</html>
