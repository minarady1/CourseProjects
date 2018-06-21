Hello Service
<?php
try {
    $oauth = new OAuth("ed183851815329a026ad2798f0489388f400099a91cde6599b16e5c525b70747","715b0752b626b981f85ff2c27825d37a24a781d33102c93008e8725612379673");
    $oauth->setToken($request_token,$request_token_secret);
    $access_token_info = $oauth->getAccessToken("https://api.lockitron.com/oauth/token");
    if(!empty($access_token_info)) {
        print_r($access_token_info);
    } else {
        print "Failed fetching access token, response was: " . $oauth->getLastResponse();
    }
} catch(OAuthException $E) {
    echo "Response: ". $E->lastResponse . "\n";
}
?>