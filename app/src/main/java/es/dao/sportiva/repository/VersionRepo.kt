package es.dao.sportiva.repository

import es.dao.sportiva.models.version.Version
import es.dao.sportiva.network.VersionApiClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VersionRepo @Inject constructor(
    private val versionApiClient: VersionApiClient,
) : GenericRepo() {

    suspend fun getLatestVersion(): Version? {
        val request = versionApiClient.getLatestVersion()
        return genericRequest(request)
    }

}