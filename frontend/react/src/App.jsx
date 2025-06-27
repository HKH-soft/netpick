import SidebarDark from "./components/my-components/SidebarDark"

function App() {

  return (
    <div className="flex h-screen w-screen">
      <div className="w-80 bg-gray-900">
        <SidebarDark />
      </div>

      <main className="h-full w-full bg-white text-black p-6 ">
        <h1 className="text-2xl font-bold">Full-Width Main Content</h1>
        <p>This area takes the full screen width.</p>
      </main>
    </div>

  )
}

export default App
