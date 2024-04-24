21package com.demo.taxi.executor;

import com.demo.taxi.executor.resp.GatekeeperResp;
import com.demo.taxi.executor.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
public class DiDiLoginExecutor implements LoginExecutor {

  private String sec_session_id;

  private final ObjectMapper mapper;

  public DiDiLoginExecutor(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @SneakyThrows
  @Override
  public Mono<String> sendCode(String phone) {
    return callGateKeeper().flatMap(this::callCodeMT);
  }

  @SneakyThrows
  @Override
  public Mono<String> login(String code) {
    if (sec_session_id == null) {
      sec_session_id = "SgTs0kxWM2yIIiYSCkdlnPfXbLryVqcabEoU8FbRD2ffNqOp2nC1OWTIRcdbGsRC";
    }
    String authToken = String.valueOf(System.currentTimeMillis() / 1000);
    String rid = RidCalculator.calc(InetAddress.getLocalHost());
    String body = STR. "{\"cell\":\"phone number\",\"code\":\"\{ code }\",\"code_type\":0,\"policy_name_list\":[\"jibengongneng\"],\"city_id\":4,\"ddfp\":\"72559f129ead26de157a07b610504a1e\",\"lat\":31.171720900319574,\"lng\":121.35947118172398,\"api_version\":\"1.0.4\",\"app_version\":\"6.7.16\",\"appid\":10000,\"canonical_country_code\":\"CN\",\"channel\":\"38\",\"country_calling_code\":\"+86\",\"country_id\":156,\"device_name\":\"devicename\",\"extra_info\":{},\"lang\":\"zh-CN\",\"map_type\":\"soso\",\"model\":\"P40\",\"network_type\":\"WIFI\",\"os\":\"10.0.0\",\"os_type\":\"android\",\"role\":1,\"scene\":1,\"sdk_version\":\"1.1.46.4\",\"\{ sec_session_id }\":\"SgTs0kxWM2yIIiYSCkdlnPfXbLryVqcabEoU8FbRD2ffNqOp2nC1OWTIRcdbGsRC\",\"utcoffset\":-1}" ;
    body = URLEncoder.encode(body, StandardCharsets.UTF_8);
    System.out.println(body);
    return WebClient.builder().baseUrl("https://epassport.diditaxi.com.cn")
        .defaultHeaders(headers -> {
          headers.add("secdd-authentication", authToken);
          headers.add("ticket", "none");
          headers.add("didi-header-omgid", CommonUtils.randomBase64UUID());
          headers.add("didi-header-ssuuid", "72559f129ead26de157a07b610504a1e");
          headers.add("Cityid", "4");
          headers.add("Productid", "0");
          headers.add("secdd-challenge", "1,com.sdu.didi.psnger|1.0.28||||0||");
          headers.add("User-Agent", "didihttp OneNet/3.0.2-TO-RABBIT com.sdu.didi.psnger/6.7.16");
          headers.add("Host", "epassport.diditaxi.com.cn");
          headers.add("_ddns_", "1");
          headers.add("didi-header-rid", rid);
          headers.add("didi-header-hint-content",
              "{\"Cityid\":4,\"app_timeout_ms\":20000,\"utc_offset\":\"480\",\"location_cityid\":4,\"lang\":\"zh-CN\",\"locale\":\"zh-CN\"}");
          headers.add("TripCountry", "CN");
          headers.add("Content-Type", "application/x-www-form-urlencoded");

        }).filter(ExchangeFilterFunction.ofRequestProcessor(req -> {
          String sign = "dd04-U8tWIad76IxucDuzEV/ut1x2JwFXzg1LOdvrgaKSGng3ImzJ4M3O5kT1LxT1KGR+dUhsWUasIyBga1pAM3WmVYimSTTVPZUb9v3qGXijQzwPJPtQkXmyrl0W5yMAabswA+j6ZjoL7awVPX4/b0fLDdlZKr2CLWnMbno/EUFXm/PWPXd7LnbLXEvF8REc";
          String wsgenv = "eV60A2OIrWk0AzMlmhn0JJADAACt2A%2F3amg0J0xyw1lFkdVvC7qW5jH44VxrQgU8uWL0B%2FiekT7D5SJn7P%2FWYBU5rQChvP4OoN30yBOC7vbSjWel6BqlsG3w6%2BhHvbANB9cxpvkYJjOHt6ZHuJSMt917mMO5ZjZKWR1CLsMSXiHvva2bz8gOkOrHx1IZnJ5oWCojGZvJTmdqwIsOc0gSeYabPx1%2FnK%2F5X2CCs%2FdwPOCCHWYbyA5DWFgWdkMGRnLTM%2FkH1c9kliSn%2Bpk1iNDQ8LiNtxYwrd2FMWPeTQFRBRvNPLJg%2BQawCskx5a6pkre%2FzinTEohwFKLAQ0bBdqvHEWWXlmS86b0c1KkXZ2cmLVlaPP%2FiKaI4ywIFoW5z91mipK1DYS6Ox1XPAJi40gpaGhw0RhX4w3778XF7V4aJi56FLmgNApZLBysBO8K%2BzCyJqtASPDyhxpYwtnmsZBdV5bcpqykcbfKesQjTV6u3qY5zaCPdVsP0EKQm1gtt1AA%2BXFMDM1vD2QFyEneVOooNtp8On0fjSoi8iXsu8fMl8lPYuMDKAlARqkxwW1ljxRdtIKZD7BtI6sonQjXeLhlUGlYvMX5xWKpM%2BZ2%2FJfAed%2BQKGfvUeYAiOQQ%2Flf4QPyQTN3xyP9fFGxsES2d%2BkmPBigLe%2FsfVHEAyiHFr%2BHPoG654i7P1wrdmaGd8bn%2F%2FpeZ1ZjYqvjeQrWxHRfo4ysNjZmMcdautO4%2BHzyJHKJ4gadTHBMt3W37eHg5iGt8gNbKbWV6MP5r8vAaXMA4wx3p35J%2F5wEmwx5f%2FtPrW1xLk9UpBy9t96nbsjSqwt5ONXhg2XQaoeCfM9WxL3ONeA%2B%2F7HgJK6%2FnMqRa1hy1rS2eT4pEZ3qIhBUwVPJwxVHRQkyHai4zWU5uWfOe7DfxEmrFSVW%2Bdo97FOCDZHRm5H1Sbh0n0K2SuKcGflgDhbaQpXbDvkoGa4DROFcgsD8R3KHy2ZsPqlNPXU0sBuHtpCwkNnNhO7xCTQieyrjim7wN6C1KVt9t8sBZnQNrPrQviTF9u1bSQ205HHOJCZysAbmUBFygWN3xOvlp3kYTI%2BerAqSaMFqfn%2BBXIU%2B2UZCZiYuJuPu5VwHjXUsXEQjpTc8ipCxgxn%2B2eQM7nnflcxYeH6VoAEmWwK%2BGDTpZIaP2Dj2xNG%2Fel6Z3jtzlUc0n40eevFljbzbqeYPZCWzsviL0NVA0WcHjliVMNx%2FeUZaPFpd39i8zqflAy2L1biWlUeYCtv3QQMxTn";
          return Mono.just(ClientRequest.from(req).url(UriComponentsBuilder.fromUri(req.url())
                  .queryParam("wsgenv", wsgenv)
                  .build(true)
                  .toUri()
              ).header("wsgsig", sign)
              .build());
        })).filter(ExchangeFilterFunction.ofRequestProcessor(req -> {
          System.out.println(req.url());
          return Mono.just(ClientRequest.from(req).build());
        }))
        .build().post().uri("/passport/login/v5/signInByCode")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(BodyInserters.fromValue("q=" + body)).retrieve().bodyToMono(String.class);
  }

  @SneakyThrows
  public Mono<GatekeeperResp> callGateKeeper() {
    String authToken = String.valueOf(System.currentTimeMillis() / 1000);
    String rid = RidCalculator.calc(InetAddress.getLocalHost());

    String body = URLEncoder.encode(
        "{\"cell\":\"phone number\",\"prefetch_cell\":\"\",\"city_id\":4,\"ddfp\":\"72559f129ead26de157a07b610504a1e\",\"lat\":31.171720900319574,\"lng\":121.35947118172398,\"api_version\":\"1.0.4\",\"app_version\":\"6.7.16\",\"appid\":10000,\"canonical_country_code\":\"CN\",\"channel\":\"38\",\"country_calling_code\":\"+86\",\"country_id\":156,\"device_name\":\"devicename\",\"extra_info\":{},\"lang\":\"zh-CN\",\"map_type\":\"soso\",\"model\":\"P40\",\"network_type\":\"WIFI\",\"os\":\"10.0.0\",\"os_type\":\"android\",\"role\":-1,\"scene\":0,\"sdk_version\":\"1.1.46.4\",\"utcoffset\":-1}",
        StandardCharsets.UTF_8);
    System.out.println(URLEncoder.encode(
        "{\"cell\":\"17673132948\",\"prefetch_cell\":\"\",\"city_id\":4,\"ddfp\":\"72559f129ead26de157a07b610504a1e\",\"lat\":31.171720900319574,\"lng\":121.35947118172398,\"api_version\":\"1.0.4\",\"app_version\":\"6.7.16\",\"appid\":10000,\"canonical_country_code\":\"CN\",\"channel\":\"38\",\"country_calling_code\":\"+86\",\"country_id\":156,\"device_name\":\"devicename\",\"extra_info\":{},\"lang\":\"zh-CN\",\"map_type\":\"soso\",\"model\":\"P40\",\"network_type\":\"WIFI\",\"os\":\"10.0.0\",\"os_type\":\"android\",\"role\":-1,\"scene\":0,\"sdk_version\":\"1.1.46.4\",\"utcoffset\":-1}",
        StandardCharsets.UTF_8));
    return WebClient.builder().baseUrl("https://epassport.diditaxi.com.cn")
        .defaultHeaders((headers) -> {
          /*headers.add("secdd-authentication",
              "532bacd92fd0b298ef1928e5d00f69ea223607f88ea2e32fbf0b9029bc81640eeb3f0310cb1256d117dd2cf6ac98f0c626dfc34c8e01000001000000");*/
          headers.add("secdd-authentication", authToken);
          headers.add("ticket", "none");
          headers.add("didi-header-omgid", CommonUtils.randomBase64UUID());
          headers.add("didi-header-ssuuid", "72559f129ead26de157a07b610504a1e");
          headers.add("Cityid", "4");
          headers.add("Productid", "0");
          headers.add("secdd-challenge", "1,com.sdu.didi.psnger|1.0.28||||0||");
          headers.add("User-Agent", "didihttp OneNet/3.0.2-TO-RABBIT com.sdu.didi.psnger/6.7.16");
          headers.add("Host", "epassport.diditaxi.com.cn");
          headers.add("_ddns_", "1");
          headers.add("didi-header-rid", rid);
          headers.add("didi-header-hint-content",
              "{\"Cityid\":4,\"app_timeout_ms\":20000,\"utc_offset\":\"480\",\"location_cityid\":4,\"lang\":\"zh-CN\",\"locale\":\"zh-CN\"}");
          headers.add("TripCountry", "CN");
          headers.add("Content-Type", "application/x-www-form-urlencoded");

        }).filter(ExchangeFilterFunction.ofRequestProcessor(req -> {
          System.out.println(req.url());
//          String sign = SignUtils.sign(req.url().toString(), body.getBytes());
          String sign = "dd04-U8tWIad76IxucDuzEV/ut1x2JwFXzg1LOdvrgaKSGng3ImzJ4M3O5kT1LxT1KGR+dUhsWUasIyBga1pAM3WmVYimSTTVPZUb9v3qGXijQzwPJPtQkXmyrl0W5yMAabswA+j6ZjoL7awVPX4/b0fLDdlZKr2CLWnMbno/EUFXm/PWPXd7LnbLXEvF8REc";
          return Mono.just(ClientRequest.from(req).header("wsgsig", sign).build());
        })).build().post().uri("/passport/login/v5/gatekeeper")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(BodyInserters.fromValue("q=" + body)).retrieve().bodyToMono(String.class)
        .mapNotNull(s -> {
          try {
            return mapper.readValue(s, GatekeeperResp.class);
          } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
          }
        });
  }

  @SneakyThrows
  public Mono<String> callCodeMT(GatekeeperResp resp) {
    String authToken = String.valueOf(System.currentTimeMillis() / 1000);
    String rid = RidCalculator.calc(InetAddress.getLocalHost());
    this.sec_session_id = resp.getSecSessionId();

    System.out.println(STR. "GatekeeperResp.sessionId == \{ resp.getSecSessionId() }" );
    String body = URLEncoder.encode(
        STR. "{\"cell\":\"phone number\",\"code_type\":0,\"city_id\":4,\"ddfp\":\"72559f129ead26de157a07b610504a1e\",\"lat\":31.171720900319574,\"lng\":121.35947118172398,\"api_version\":\"1.0.4\",\"app_version\":\"6.7.16\",\"appid\":10000,\"canonical_country_code\":\"CN\",\"channel\":\"38\",\"country_calling_code\":\"+86\",\"country_id\":156,\"device_name\":\"devicename\",\"extra_info\":{},\"lang\":\"zh-CN\",\"map_type\":\"soso\",\"model\":\"P40\",\"network_type\":\"WIFI\",\"os\":\"10.0.0\",\"os_type\":\"android\",\"role\":1,\"scene\":1,\"sdk_version\":\"1.1.46.4\",\"sec_session_id\":\"\{ resp.getSecSessionId() }\",\"utcoffset\":-1}" ,
        StandardCharsets.UTF_8);
    return WebClient.builder().baseUrl("https://epassport.diditaxi.com.cn")
        .defaultHeaders((headers) -> {
//          headers.add("secdd-authentication",
//              "532bacd92fd0b298ef1928e5d00f69ea223607f88ea2e32fbf0b9029bc81640eeb3f0310cb1256d117dd2cf6ac98f0c626dfc34c8e01000001000000");
          headers.add("secdd-authentication", authToken);

          headers.add("ticket", "none");
          headers.add("didi-header-omgid", CommonUtils.randomBase64UUID());
          headers.add("didi-header-ssuuid", "72559f129ead26de157a07b610504a1e");
          headers.add("Cityid", "4");
          headers.add("Productid", "0");
          headers.add("secdd-challenge", "1,com.sdu.didi.psnger|1.0.28||||0||");
          headers.add("User-Agent", "didihttp OneNet/3.0.2-TO-RABBIT com.sdu.didi.psnger/6.7.16");
          headers.add("Host", "epassport.diditaxi.com.cn");
          headers.add("_ddns_", "1");
          headers.add("didi-header-rid", rid);
          headers.add("didi-header-hint-content",
              "{\"Cityid\":4,\"app_timeout_ms\":20000,\"utc_offset\":\"480\",\"location_cityid\":4,\"lang\":\"zh-CN\",\"locale\":\"zh-CN\"}");
          headers.add("TripCountry", "CN");
          headers.add("wsgsig",
              "dd04-U8tWIad76IxucDuzEV/ut1x2JwFXzg1LOdvrgaKSGng3ImzJ4M3O5kT1LxT1KGR+dUhsWUasIyBga1pAM3WmVYimSTTVPZUb9v3qGXijQzwPJPtQkXmyrl0W5yMAabswA+j6ZjoL7awVPX4/b0fLDdlZKr2CLWnMbno/EUFXm/PWPXd7LnbLXEvF8REc");
          headers.add("Content-Type", "application/x-www-form-urlencoded");
        }).build().post().uri("/passport/login/v5/codeMT")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(BodyInserters.fromValue("q=" + body)).retrieve().bodyToMono(String.class);
  }

}
