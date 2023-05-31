export interface Archive {
    name: string
    title: string
    comments: string
    archive: File
}

export interface Bundle {
    bundleId: string
    name: string
    title: string
    comments: string
    date: Date
    urls: string[]
}