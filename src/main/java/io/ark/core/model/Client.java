package io.ark.core.model;

import java.net.URL;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {

  private String token;
  private String symbol;
  private URL explorer;

}
