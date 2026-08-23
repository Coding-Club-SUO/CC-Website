'use cache';

import ResourcesPage from '../../views/resourcepage/ResourcesPage'
import { getResources, type Resource } from '../../api/resources/resources'

export default async function Resources() {
  let resources : Resource[] = []

  try {
    resources = await getResources()
  } catch {
    resources = []
  }

  return <ResourcesPage resources={resources} />
}