function verify() {
    var email = document.forms['form']['email'].value;
    if(email.length <=10){
        document.getElementById("error").innerHTML = "Email is too short";
        return false;
    }
    var password1 = document.forms['form']['password'].value;
    var password2 = document.forms['form']['verifyPassword'].value;
    if (password1 == null || password1 === "" || password1 !== password2) {
        document.getElementById("error").innerHTML = "Please check your passwords";
        return false;
    }
    document.forms['form'].submit();
    return true;
}