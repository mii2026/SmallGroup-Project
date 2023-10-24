package com.server.smallgroup;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {
    private String apiKey = "NCSH6EY6VHJS8X2M";
    private String apiSecretKey = "NJKMO6JHCFK7RQUQEDUEO4QQXZUCMOLW";

    private DefaultMessageService messageService;

    @PostConstruct
    private void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }

    // 단일 메시지 발송 예제
    public SingleMessageSentResponse sendVerificationCode(String to, String verificationCode) {
        Message message = new Message();
        message.setFrom("01026003140");
        message.setTo(to);
        message.setText("[인증번호] " + verificationCode);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        return response;
    }

    public SingleMessageSentResponse sendPW(String to, String pw) {
        Message message = new Message();
        message.setFrom("01026003140");
        message.setTo(to);
        message.setText("[임시 비밀번호]" + pw);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        return response;
    }

}