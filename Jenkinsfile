pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    parameters {
        choice(name: 'TEST_ENV', choices: ['dev', 'staging', 'prod'], description: 'Target environment configuration (environments/<env>.properties)')
        booleanParam(name: 'RUN_UI_TESTS', defaultValue: true, description: 'Execute the Selenium UI regression suite')
        booleanParam(name: 'RUN_API_TESTS', defaultValue: true, description: 'Execute the REST Assured microservices API suite')
        booleanParam(name: 'RUN_MESSAGING_TESTS', defaultValue: false, description: 'Execute the Kafka event-driven suite (requires a reachable broker)')
    }

    environment {
        MAVEN_OPTS = '-Dmaven.repo.local=.m2/repository'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Static Analysis') {
            steps {
                sh 'mvn -B -ntp clean install -DskipTests'
                sh 'mvn -B -ntp checkstyle:check'
            }
        }

        stage('API Tests') {
            when { expression { params.RUN_API_TESTS } }
            steps {
                sh """
                    mvn -B -ntp -pl api-tests -am test \
                        -Denv=${params.TEST_ENV} \
                        -DsuiteXmlFile=src/test/resources/testng-api.xml
                """
            }
        }

        stage('UI Tests') {
            when { expression { params.RUN_UI_TESTS } }
            steps {
                sh """
                    mvn -B -ntp -pl ui-tests -am test \
                        -Denv=${params.TEST_ENV} \
                        -Dbrowser.headless=true \
                        -Dselenium.grid.enabled=true \
                        -Dselenium.grid.url=http://selenium-hub:4444/wd/hub \
                        -DsuiteXmlFile=src/test/resources/testng-ui.xml
                """
            }
        }

        stage('Messaging Tests') {
            when { expression { params.RUN_MESSAGING_TESTS } }
            steps {
                sh """
                    mvn -B -ntp -pl messaging-tests -am test \
                        -Denv=${params.TEST_ENV} \
                        -DsuiteXmlFile=src/test/resources/testng-messaging.xml
                """
            }
        }

        stage('Publish Allure Report') {
            steps {
                allure includeProperties: false, jdk: '', results: [[path: '**/target/allure-results']]
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
            archiveArtifacts artifacts: '**/target/screenshots/**', allowEmptyArchive: true
        }
        failure {
            echo 'Pipeline failed - notify #qa-automation Slack channel'
        }
    }
}
