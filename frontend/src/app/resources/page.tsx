'use cache';
import ResourcesPage from '../../views/resourcepage/ResourcesPage'
import { getResources, type Resource } from '../../api/resources/resources'
import { logger } from '../../lib/logger'

export default async function Resources() {
  let resources: Resource[] = []
  let loadFailed = false

  try {
    resources = await getResources()
  } catch (error) {
    resources = []
    loadFailed = true
    logger.error({ err: error }, 'Failed to fetch resources')
  }

  return <ResourcesPage resources={resources} loadFailed={loadFailed} />
}
