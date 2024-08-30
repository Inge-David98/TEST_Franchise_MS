package co.com.franchise.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
@Value
@Jacksonized
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Branch {
    @JsonProperty("branchName")
    @JsonInclude(NON_NULL)
    String branchName;
    List<Product> products;
}
