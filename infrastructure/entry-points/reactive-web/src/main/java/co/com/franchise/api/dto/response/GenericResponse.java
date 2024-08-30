package co.com.franchise.api.dto.response;

import co.com.franchise.model.utils.TechnicalMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenericResponse<T> {
    private String code;
    private String message;
    private T data;

    public static <T> GenericResponse<T> success(T object) {
        return new GenericResponse<>(TechnicalMessage.SUCCESS.getExternalCode(),
                TechnicalMessage.SUCCESS.getExternalMessage(), object);
    }
}
