node{

	stage ('SCM Checkout'){
	
	git 'https://github.com/bdushimi/cdci-jenkins'

	}

	stage('Compile-Package'){
	
	def mvnHome = tool name: 'Maven', type: 'maven'
	sh "${mvnHome}/bin/mvn package"

	}

}
