package es.thalesalv.cartservice.cart.adapter.bean;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;
    private final ObjectMapper objectMapper;

    @Bean
    public WebClient webClient(ClientRegistrationRepository clientRegistrations,
            OAuth2AuthorizedClientRepository auth2AuthorizedClients) {

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder().codecs(
                configurer -> configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper)))
                .build();

        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                clientRegistrations, auth2AuthorizedClients);

        oauth2.setDefaultClientRegistrationId("pricing-client");
        return WebClient.builder().apply(oauth2.oauth2Configuration()).exchangeStrategies(exchangeStrategies)
                .filter(lbFunction).baseUrl("http://pricing/pricing/price").build();
    }
}
