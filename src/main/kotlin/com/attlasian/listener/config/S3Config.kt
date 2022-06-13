package com.attlasian.listener.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Config {

    @Value("\${cloud.aws.region.static}")
    private val region: String? = null

    @Value("\${cloud.aws.credentials.access-key}")
    private val accessKeyId: String? = null

    @Value("\${cloud.aws.credentials.secret-key}")
    private val secretAccessKey: String? = null

    @Value("\${cloud.aws.queue.uri}")
    private val sqsUrl: String? = null

    @Bean
    @Qualifier("instanceS3")
    fun createInstanceAmazonS3(): AmazonS3? {

        return AmazonS3ClientBuilder
            .standard()
            .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(sqsUrl, region))
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(accessKeyId, secretAccessKey)))
            .enablePathStyleAccess()
            .build()
    }
}
