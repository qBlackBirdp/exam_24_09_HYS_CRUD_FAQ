<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="회원가입"></c:set>
<%@ include file="../common/head.jspf"%>

<hr />

<section class="section">
	<form action="../member/doJoin" method="POST" onsubmit="return validateForm();">
		<table class="form-table">
			<tbody>
				<tr>
					<th>아이디</th>
					<td><input name="loginId" autocomplete="off" type="text"
						placeholder="아이디를 입력해주세요" required /></td>
				</tr>
				<tr>
					<th>비밀번호</th>
					<td><input name="loginPw" id="loginPw" autocomplete="off"
						type="text" placeholder="비밀번호를 입력해" required /></td>
				</tr>
				<tr>
					<th>비밀번호 확인</th>
					<td><input name="loginPwConfirm" id="loginPwConfirm"
						autocomplete="off" type="text" placeholder="비밀번호를 다시 입력해"
						required /></td>
				</tr>
				<tr>
					<th>이름</th>
					<td><input name="name" autocomplete="off" type="text"
						placeholder="이름을 입력해주세요" required /></td>
				</tr>
				<tr>
					<th>닉네임</th>
					<td><input name="nickname" autocomplete="off" type="text"
						placeholder="닉네임을 입력해주세요" required /></td>
				</tr>
				<tr>
					<th>전화번호</th>
					<td><input name="cellphoneNum" autocomplete="off" type="text"
						placeholder="전화번호를 입력해주세요" required /></td>
				</tr>
				<tr>
					<th>이메일</th>
					<td><input name="email" autocomplete="off" type="email"
						placeholder="이메일을 입력해주세요" required /></td>
				</tr>
				<tr>
					<th></th>
					<td><input type="submit" value="회원가입" /></td>

				</tr>
			</tbody>
		</table>
	</form>
</section>

<script>
    const pwField = document.getElementById("loginPw");
    const pwConfirmField = document.getElementById("loginPwConfirm");
    const submitBtn = document.getElementById("submitBtn");

    function checkPasswords() {
        if (pwField.value === pwConfirmField.value) {
            pwField.style.borderColor = "green";
            pwConfirmField.style.borderColor = "green";
            submitBtn.disabled = false;
        } else {
            pwField.style.borderColor = "red";
            pwConfirmField.style.borderColor = "red";
            submitBtn.disabled = true;
        }
    }

    function validateForm() {
        if (pwField.value !== pwConfirmField.value) {
            alert("비밀번호가 일치하지 않습니다. 확인해주세요.");
            return false; // 폼 제출을 막습니다.
        }
        return true;
    }

    pwField.addEventListener("input", checkPasswords);
    pwConfirmField.addEventListener("input", checkPasswords);
</script>

</body>
</html>