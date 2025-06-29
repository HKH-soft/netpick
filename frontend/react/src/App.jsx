import SidebarDark from "./components/my-components/SidebarDark"
import { useEffect , useState } from "react"
import { getCustomers } from "./services/client"
import Card from "./components/my-components/Card"

function App() {

  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true)
    getCustomers().then(res => {
      setCustomers(res.data)
    }) .catch(err => {
      console.log(err)
    }).finally(() => {
      setLoading(false)
    })
  },[])

  if(loading){
    return (
      <div className="flex h-screen w-screen relative">
        {/* Sidebar */}
        <div className="w-80 bg-gray-900 shrink-0">
          <SidebarDark />
        </div>

        {/* Main content */}
        <main className="h-full w-full bg-white text-black p-6">
          {/* Your main content here */}
        </main>

        {/* Fullscreen blurred loading overlay */}
        <div className="fixed inset-0 z-50 backdrop-blur-sm bg-black/40 flex items-center justify-center">
          <div
            className="w-12 h-12 rounded-full animate-spin border-4 border-solid border-gray-900 border-t-transparent"
            aria-label="Loading"
          ></div>
        </div>
      </div>

    )
  }
  
  // if(customers.length === 0){
  //   return (
  //   <div className="flex h-screen w-screen">
  //     <div className="w-80 bg-gray-900">
  //       <SidebarDark />
  //     </div>

  //     <main className="h-full w-full bg-white text-black p-6 ">
  //       <h1>no fucking way</h1>
  //     </main>
  //   </div>
  //   )
  // }

  return (
    <div className="flex h-screen w-screen">
     {/* Sidebar */}
      <SidebarDark />

      {/* Main Content */}
      <main className="ml-80 flex-1 min-h-screen bg-white text-black p-6 overflow-y-auto">
        <div className="mt-6 grid grid-cols-1 gap-x-8 gap-y-8 sm:grid-cols-2 sm:gap-y-10 lg:grid-cols-4">
          {customers.map((customer, index) => (
            <Card
              key={index}
              id={customer.name}
              name={customer.name}
              category={customer.email}
              href={customer.name}
              price={customer.age}
              imageSrc={customer.gender == false? `https://randomuser.me/api/portraits/women/${index + 1}.jpg` : `https://randomuser.me/api/portraits/men/${index + 1}.jpg`}
              imageAlt={customer.gender}
            />
          ))}
        </div>
      </main>
    </div>

  )
}

export default App
