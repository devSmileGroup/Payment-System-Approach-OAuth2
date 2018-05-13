package com.white.loginspringdata.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.liqpay.LiqPay;
import com.liqpay.LiqPayUtil;

@RestController
public class LiqPayController {

	@RequestMapping(value = {"/payment"}, method = RequestMethod.GET)
	public String welcomePage(Model model) {


		Map<String, String> params = new HashMap<>();
		params.put("amount", "1000");
		params.put("currency", "UAH");
		params.put("description", "Book 'Thinking in Java'");
		params.put("order_id", "123456789");
		params.put("sandbox", "1"); // enable the testing environment and card will NOT charged. If
									// not set will be used property isCnbSandbox()
		LiqPay liqpay = new LiqPay("publicKey", "privateKey");
		String html = liqpay.cnb_form(params);
		System.out.println(html);

		return html;
	}

	@RequestMapping(value = "/result", method = RequestMethod.POST)
	public void result(@RequestParam(value = "data") String data,
			@RequestParam(value = "signature") String signature) {

		System.out.println(":)");

		System.out.println("data: " + data + "; signature: " + signature);

		String serverSignature =
				LiqPayUtil.base64_encode(LiqPayUtil.sha1("param" + data + "param"));

		if (serverSignature.equals(signature)) {

		}
	}

}
