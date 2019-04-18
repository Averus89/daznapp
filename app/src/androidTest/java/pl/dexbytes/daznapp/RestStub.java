package pl.dexbytes.daznapp;

import pl.dexbytes.daznapp.constants.ServerConfig;
import pl.dexbytes.daznapp.net.BaseUrlChangingInterceptor;
import pl.dexbytes.daznapp.utils.AssetReaderUtil;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

public class RestStub{
    public static void stubEventsEndpointWithError(int errorCode){
        String url = "/getEvents/";
        BaseUrlChangingInterceptor.get().setInterceptor(ServerConfig.LOCAL_HOST + url);
        stubFor(get(urlPathMatching(url)).willReturn(aResponse().withStatus(errorCode).withFixedDelay(5000)));
    }

    public static void stubEventsWithBody(){
        String url = "/getEvents/";
        BaseUrlChangingInterceptor.get().setInterceptor(ServerConfig.LOCAL_HOST + url);
        String jsonBody = AssetReaderUtil.asset("getEvents.json");
        stubFor(get(urlPathMatching(url)).willReturn(aResponse().withStatus(200).withBody(jsonBody).withFixedDelay(5000)));
    }
}
