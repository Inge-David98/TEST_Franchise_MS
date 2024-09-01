package co.com.franchise.model.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {
    SUCCESS("0", "0", "SUCCESS", "SUCCESS");
    private final String code;
    private final String externalCode;
    private final String message;
    private final String externalMessage;
}
