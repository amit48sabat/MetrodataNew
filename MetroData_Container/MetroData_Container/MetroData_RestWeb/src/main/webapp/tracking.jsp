<!DOCTYPE html>
<html>
<head>
<title>Hello WebSocket</title>
<link
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
	crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.3.1.js"
	integrity="sha256-2Kok7MbOyxpgUVvAk/HJ2jigOSYS2auK4Pfzbm7uH60="
	crossorigin="anonymous"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.2.0/sockjs.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.js"></script>
<script>
	var stompClient = null;

	function setConnected(connected) {
		$("#connect").prop("disabled", connected);
		$("#disconnect").prop("disabled", !connected);
		if (connected) {
			$("#conversation").show();
		} else {
			$("#conversation").hide();
		}
		$("#greetings").html("");
	}

	function connect() {
		var socket = new SockJS('/MetroData_RestWeb/rest/gs-guide-websocket');
		stompClient = Stomp.over(socket);

		/* stompClient.connect({
			"user" : document.getElementById("login").value
		}

		, function(frame) {
			setConnected(true);
			console.log('Connected: ' + frame);
			stompClient.subscribe('/user/queue/reply', function(greeting) {
				showGreeting(JSON.parse(greeting.body).content);
			});
		}); */

		 	stompClient.connect({}, function(frame) {
				setConnected(true);
				console.log('Connected: ' + frame);
				stompClient.subscribe('/topic/T001', function(dto) {
					showGreeting(dto);
				});
			}); 
	}

	function disconnect() {
		if (stompClient !== null) {
			stompClient.disconnect();
		}
		setConnected(false);
		console.log("Disconnected");
	}

	/* function sendName() {
		stompClient.send("/app/hello", {}, JSON.stringify({
			'name' : $("#name").val()
		}));
	} */

	function sendName() {
		stompClient.send("/rest/hello", {}, JSON.stringify({
			'name' : $("#name").val(),
			'toUser' : $("#name").val()
		}));
	}

	function showGreeting(message) {
		$("#greetings").append("<tr><td>" + message + "</td></tr>");
	}

	$(function() {
		$("form").on('submit', function(e) {
			e.preventDefault();
		});
		$("#connect").click(function() {
			connect();
		});
		$("#disconnect").click(function() {
			disconnect();
		});
		$("#send").click(function() {
			sendName();
		});
	});
</script>
</head>
<body>
	<noscript>
		<h2 style="color: #ff0000">Seems your browser doesn't support
			Javascript! Websocket relies on Javascript being enabled. Please
			enable Javascript and reload this page!</h2>
	</noscript>
	<div id="main-content" class="container">
		<div class="row">
			<div class="col-md-6">
				<form class="form-inline">
					<div class="form-group">
						<label for="connect">WebSocket connection:</label> <input
							id="login" type="text" />
						<button id="connect" class="btn btn-default" type="submit">Connect</button>
						<button id="disconnect" class="btn btn-default" type="submit"
							disabled="disabled">Disconnect</button>
					</div>
				</form>
			</div>
			<div class="col-md-6">
				<form class="form-inline">
					<div class="form-group">
						<label for="name">What is your name?</label> <input type="text"
							id="name" class="form-control" placeholder="Your name here...">
					</div>
					<button id="send" class="btn btn-default" type="submit">Send</button>
				</form>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table id="conversation" class="table table-striped">
					<thead>
						<tr>
							<th>Greetings</th>
						</tr>
					</thead>
					<tbody id="greetings">
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>